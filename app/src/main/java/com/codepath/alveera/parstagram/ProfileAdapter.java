package com.codepath.alveera.parstagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.alveera.parstagram.model.GlideApp;
import com.codepath.alveera.parstagram.model.Post;
import com.parse.ParseUser;

import java.util.List;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private List<Post> mPosts;
    Context context;
    // pass in the Tweets array in the constructor
    public ProfileAdapter(List<Post> posts) {
        mPosts = posts;
    }

    // for each row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_pic, parent, false);
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

        GlideApp.with(context)
                .load(post.getImage().getUrl())
                //.transform(new CropSquareTransformation(75, 10))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }



    // create a ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ivProfPost);
        }
        @Override
        public void onClick(View view) {

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
