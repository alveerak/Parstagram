package com.codepath.alveera.parstagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.codepath.alveera.parstagram.model.Post;

public class InstaActivity extends AppCompatActivity {
    HomeFragment homeFrag;
    ProfileFragment profFrag;
    PicFragment picFrag;
    Fragment frag;

    private FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    frag = homeFrag;
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.my_fragment, frag);
                    ft.commit();
                    return true;
                case R.id.navigation_dashboard:
                    frag = picFrag;
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.my_fragment, frag);
                    ft.commit();
                    return true;
                case R.id.navigation_notifications:
                    frag = profFrag;
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.my_fragment, frag);
                    ft.commit();
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

        homeFrag = new HomeFragment();
        profFrag = new ProfileFragment();
        picFrag = new PicFragment();
        frag = homeFrag;
        ft.replace(R.id.my_fragment, frag);
        ft.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void goToDetails(Post post){
                Fragment fragment = new PostDetailsFragment();
                Bundle args = new Bundle();
                args.putString("id", post.getObjectId());
                fragment.setArguments(args);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.my_fragment, fragment);
                fragmentTransaction.commit();
    }

    public void goBackToFeed() {
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.my_fragment, fragment);
        fragmentTransaction.commit();
    }

}
