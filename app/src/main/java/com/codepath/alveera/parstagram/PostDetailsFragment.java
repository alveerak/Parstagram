package com.codepath.alveera.parstagram;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.alveera.parstagram.model.GlideApp;
import com.codepath.alveera.parstagram.model.Post;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class PostDetailsFragment extends Fragment {
    //        public ImageView ivProfileImage;
//        public TextView tvUsername;
//        public TextView tvBody;
//        public TextView tvTimeElapsed;
//        public TextView tvHandle;
//        public ImageView ivRetweetsImage;
//        public TextView tvNumRetweets;
//        public ImageView ivFavoritesImage;
//        public TextView tvNumFavorites;
//        public EditText ptReplyTweet;
//        public ImageButton ibReply;
    public TextView description;
    public ImageView image;
    public TextView timeElapsed;
    public TextView username;
    public ImageView detailsProfPic;
    Context context;
    public Button back;

    public EditText typeComment;
    public Button postComment;
    public CommentAdapter cAdapter;
    public RecyclerView rvComments;
    public List<Object> commentList;

    public PostDetailsFragment() {

        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        context = getContext();
        // perform findViewById

//            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
//            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
//            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
//            tvTimeElapsed = (TextView) itemView.findViewById(R.id.tvTimeElapsed);
//            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
//            ivRetweetsImage = (ImageView) itemView.findViewById(R.id.ivRetweetsImage);
//            tvNumRetweets = (TextView) itemView.findViewById(R.id.tvNumRetweets);
//            ivFavoritesImage = (ImageView) itemView.findViewById(R.id.ivFavoritesImage);
//            tvNumFavorites = (TextView) itemView.findViewById(R.id.tvNumFavorites);
//            ptReplyTweet = (EditText) itemView.findViewById(R.id.ptReplyTweet);
//            ibReply = (ImageButton) itemView.findViewById(R.id.ibReply);
//            itemView.setOnClickListener(this);
        description = (TextView) view.findViewById(R.id.tvPostDescription);
        image = (ImageView) view.findViewById(R.id.ivPostPic);
        timeElapsed = (TextView) view.findViewById(R.id.tvPostTimeStamp);
        username = (TextView) view.findViewById(R.id.tvPostUsername);
        back = (Button) view.findViewById(R.id.back_btn);
        detailsProfPic = (ImageView) view.findViewById(R.id.details_prof_pic);

        typeComment = (EditText) view.findViewById(R.id.et_comment);
        postComment = (Button) view.findViewById(R.id.submit_comment);

        rvComments = (RecyclerView) view.findViewById(R.id.rvComments);

        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        commentList = new ArrayList<>();
        cAdapter = new CommentAdapter(commentList);
        rvComments.setAdapter(cAdapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InstaActivity) context).goBackToFeed();

            }
        });

        Bundle b = getArguments();
        String id  = b.getString("id");
        Post.Query postsQuery = new Post.Query();
        postsQuery.getQuery(Post.class).getInBackground(id, new GetCallback<Post>() {

            @Override
            public void done(final Post post, ParseException e) {


                description.setText(post.getDescription());
                GlideApp.with(context)
                        .load(post.getImage().getUrl())
                        .into(image);

                try {
                    ParseFile profilePic = post.getUser().fetchIfNeeded().getParseFile("profPic");
                    if (profilePic != null) {
                        GlideApp.with(context)
                                .load(profilePic.getUrl())
                                .circleCrop()
                                //.transform(new RoundedCornersTransformation(75, 0))
                                .into(detailsProfPic);
                    }

                    postComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String initialComment = ParseUser.getCurrentUser().getObjectId() +
                                    " " + typeComment.getText();
                            post.add("comments", initialComment);
                            post.saveInBackground();
                            typeComment.setText("");

                            ((InstaActivity) context).goToDetails(post);
                        }
                    });

                    commentList.clear();
                    commentList.addAll(post.getList("comments"));
                    cAdapter.notifyDataSetChanged();



                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                timeElapsed.setText(post.getRelativeTimeAgo());
                try {
                    username.setText(post.getUser().fetchIfNeeded().getUsername());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_details, container, false);
    }

}
