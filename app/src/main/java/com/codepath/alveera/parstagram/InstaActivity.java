package com.codepath.alveera.parstagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.codepath.alveera.parstagram.model.Post;

public class InstaActivity extends AppCompatActivity {

    private TextView mTextMessage;
    //Fragment currentFragment = null;
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

                    //FragmentManager manager = getSupportFragmentManager();
                    //manager.beginTransaction().replace(R.id.fragment_home, new HomeFragment()).commit();
                    // mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    frag = picFrag;
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.my_fragment, frag);
                    ft.commit();
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    frag = profFrag;
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.my_fragment, frag);
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

        homeFrag = new HomeFragment();
        profFrag = new ProfileFragment();
        picFrag = new PicFragment();
        frag = homeFrag;
        ft.replace(R.id.my_fragment, frag);
        ft.commit();

        mTextMessage = (TextView) findViewById(R.id.message);
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
