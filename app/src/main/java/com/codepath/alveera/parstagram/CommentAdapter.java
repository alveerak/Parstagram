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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Object> mComments;
    Context context;
    public ParseUser user;
    public String comment;
    // pass in the Tweets array in the constructor
    public CommentAdapter(List<Object> comment) {
        mComments = comment;
    }

    // for each row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_comment, parent, false);
        CommentAdapter.ViewHolder viewHolder = new CommentAdapter.ViewHolder(postView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String commentString = (String) mComments.get(position);
        final String [] commentArray;
        commentArray = commentString.split(" ", 2);
        comment = commentArray[1];
        ParseQuery<ParseUser> pq = ParseUser.getQuery().whereEqualTo("objectId", commentArray[0]);
        pq.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for (int i = 0; i < objects.size(); i++) {
                    user = objects.get(i);
                    holder.tvComment.setText(commentArray[1]);
                    ParseFile profilePic = user.getParseFile("profPic");
                    if (profilePic != null) {
                        GlideApp.with(context)
                                .load(profilePic.getUrl())
                                .circleCrop()
                                .into(holder.commentProfPic);
                        }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }



    // create a ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView commentProfPic;
        public TextView tvComment;

        public ViewHolder(View itemView) {
            super(itemView);
            commentProfPic = (ImageView) itemView.findViewById(R.id.comment_pic);
            tvComment = (TextView) itemView.findViewById(R.id.tv_comment);
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mComments.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mComments.addAll(list);
        notifyDataSetChanged();
    }



}
