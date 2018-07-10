package com.codepath.alveera.parstagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.alveera.parstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        loadTopPosts();
    }

    public void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();


        postsQuery.getQuery(Post.class).findInBackground(new FindCallback<Post>() {

            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        ParseUser p = objects.get(i).getUser();
                        try {
                            Log.d("InstaActivity", "Pose[" + i + "] = " +
                                    objects.get(i).getDescription() +
                                    "\nusername = " + objects.get(i).getUser().fetchIfNeeded().getUsername());
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                    }
                }else {
                    e.printStackTrace();
                }
            }
        });
    }


}
