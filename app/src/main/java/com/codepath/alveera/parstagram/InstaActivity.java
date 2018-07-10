package com.codepath.alveera.parstagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class InstaActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Fragment currentFragment = null;
    private FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    currentFragment = new HomeFragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.my_fragment, currentFragment);
                    ft.commit();

                    //FragmentManager manager = getSupportFragmentManager();
                    //manager.beginTransaction().replace(R.id.fragment_home, new HomeFragment()).commit();
                    // mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    currentFragment = new PicFragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.my_fragment, currentFragment);
                    ft.commit();
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    currentFragment = new ProfileFragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.my_fragment, currentFragment);
                    ft.commit();
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta);

        ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new HomeFragment();
        ft.replace(R.id.my_fragment, currentFragment);
        ft.commit();

        //loadTopPosts();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



//    public void loadTopPosts() {
//        final Post.Query postsQuery = new Post.Query();
//        postsQuery.getTop().withUser();
//
//
//        postsQuery.getQuery(Post.class).findInBackground(new FindCallback<Post>() {
//
//            @Override
//            public void done(List<Post> objects, ParseException e) {
//                if (e == null) {
//                    for (int i = 0; i < objects.size(); ++i) {
//                        ParseUser p = objects.get(i).getUser();
//                        try {
//                            Log.d("InstaActivity", "Pose[" + i + "] = " +
//                                    objects.get(i).getDescription() +
//                                    "\nusername = " + objects.get(i).getUser().fetchIfNeeded().getUsername());
//                        } catch (ParseException e1) {
//                            e1.printStackTrace();
//                        }
//
//                    }
//                }else {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

}
