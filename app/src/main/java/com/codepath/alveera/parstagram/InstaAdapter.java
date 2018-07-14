package com.codepath.alveera.parstagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.alveera.parstagram.model.GlideApp;
import com.codepath.alveera.parstagram.model.Post;
import com.parse.ParseUser;

import java.util.List;


public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder> {

    private List<Post> mPosts;
    Context context;
    // pass in the Tweets array in the constructor
    public InstaAdapter(List<Post> posts) {
        mPosts = posts;
    }

    // for each row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    interface Handler{
        void onClick(Post tweet, Context context);
    }


    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // get the data according to position
        final Post post = mPosts.get(position);
        ParseUser user = post.getUser();
        holder.description.setText(post.getDescription());
        holder.username.setText(user.getUsername());
        holder.timeElapsed.setText(post.getRelativeTimeAgo());

        GlideApp.with(context)
                .load(post.getImage().getUrl())

                //.transform(new CropSquareTransformation(75, 10))
                .into(holder.image);

        if (user.getParseFile("profPic") != null) {
            GlideApp.with(context)
                    .load(user.getParseFile("profPic").getUrl())
                    .circleCrop()
                    .into(holder.postProfPic);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }



    // create a ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView description;
        public ImageView image;
        public TextView timeElapsed;
        public TextView username;
        public ImageView postProfPic;



        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById
            description = (TextView) itemView.findViewById(R.id.tvDescription);
            image = (ImageView) itemView.findViewById(R.id.ivPic);
            timeElapsed = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            username = (TextView) itemView.findViewById(R.id.tvUsername);
            postProfPic = (ImageView) itemView.findViewById(R.id.iv_post_prof_pic);
            itemView.setOnClickListener(this);
        }

        // when the user clicks on a row, show MovieDetailsActivity for the selected Movie
        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Post post = mPosts.get(position);
                ((InstaActivity) context).goToDetails(post);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}
