package com.codepath.alveera.parstagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.alveera.parstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ArrayList<Post> posts;
    InstaAdapter pAdapter;
    private SwipeRefreshLayout swipeContainer;
    public RecyclerView rvPosts;
    private ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // on some click or some loading we need to wait for...
        pb = (ProgressBar) view.findViewById(R.id.pbLoadingHome);
        posts = new ArrayList<>();
        pAdapter = new InstaAdapter(posts);
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);

        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(pAdapter);



        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        loadTopPosts();
    }

    public void fetchTimelineAsync(int page) {
        pAdapter.clear();
        loadTopPosts();
        swipeContainer.setRefreshing(false);

    }

    public void loadTopPosts() {



        final Post.Query postsQuery = new Post.Query();

        postsQuery.getQuery(Post.class).orderByAscending("createdAt").findInBackground(new FindCallback<Post>() {

            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    Post post = new Post();
                    pb.setVisibility(ProgressBar.VISIBLE);
                    for (int i = 0; i < objects.size(); ++i) {
                        ParseUser p = objects.get(i).getUser();
                        try {
                            Log.d("InstaActivity", "Pose[" + i + "] = " +
                                    objects.get(i).getDescription() +
                                    "\nusername = " + objects.get(i).getUser().fetchIfNeeded().getUsername());
                            posts.add(0, objects.get(i));
                            //pAdapter.notifyItemInserted(0);
                            pAdapter.notifyItemInserted(0);

                            pb.setVisibility(ProgressBar.INVISIBLE);

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
