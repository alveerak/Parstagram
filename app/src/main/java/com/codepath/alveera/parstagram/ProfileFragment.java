package com.codepath.alveera.parstagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.alveera.parstagram.model.GlideApp;
import com.codepath.alveera.parstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProfileFragment extends Fragment {
    ArrayList<Post> profPosts;
    ProfileAdapter profPostAdapter;
    public RecyclerView rvProfile;
    private Button logout;
    private Button takePic;
    private Button submitPic;
    private ImageView profThumbnail;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_OK = 1;
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private File photoFile;
    private String imagePath = "";
    private TextView tvBio;
    private TextView tvProfUsername;
    private Button updateBio;
    private EditText typeBio;
    private ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // on some click or some loading we need to wait for...
        pb = (ProgressBar) view.findViewById(R.id.pbLoadingProfile);
        profPosts = new ArrayList<>();
        profPostAdapter = new ProfileAdapter(profPosts);
        rvProfile = (RecyclerView) view.findViewById(R.id.rvProfile);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(getContext(), 3);
        rvProfile.setLayoutManager(layout);

        rvProfile.setAdapter(profPostAdapter);

        loadTopProfPosts();

        tvProfUsername = (TextView) view.findViewById(R.id.tvProfileUsername);
        tvBio = (TextView) view.findViewById(R.id.details_description);
        updateBio = (Button)view.findViewById(R.id.updateBio);
        typeBio = (EditText) view.findViewById(R.id.type_bio);


        logout = (Button) view.findViewById(R.id.logout_btn);
        takePic = (Button) view.findViewById(R.id.take_prof_pic_btn);
        submitPic = (Button) view.findViewById(R.id.upload_prof_pic_btn);
        profThumbnail = (ImageView) view.findViewById(R.id.iv_prof_pic) ;
        tvProfUsername.setText(ParseUser.getCurrentUser().getUsername());
        tvBio.setText(ParseUser.getCurrentUser().getString("bio"));

        ParseFile profilePic = null;
        try {
            profilePic = ParseUser.getCurrentUser().fetchIfNeeded().getParseFile("profPic");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (profilePic != null) {
            GlideApp.with(this)
                    .load(profilePic.getUrl())
                    .circleCrop()
                    //.transform(new RoundedCornersTransformation(75, 0))
                    .into(profThumbnail);
        }

        updateBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.getCurrentUser().put("bio", typeBio.getText().toString());
                ParseUser.getCurrentUser().saveInBackground();
                typeBio.setText("");
                tvBio.setText(ParseUser.getCurrentUser().getString("bio"));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                // go back to log in screen
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        submitPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ParseUser user = ParseUser.getCurrentUser();

                //photoFile = new File(imagePath);
                final ParseFile parseFile = new ParseFile(photoFile);

                uploadProfilePic(parseFile, user);

            }
        });

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File media;
                //String imagePath;
                try{
                    String time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(new Date());
                    File directory = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    //String name =
                    media = File.createTempFile("IMG"+time, ".jpg", directory);
                    imagePath = media.getAbsolutePath();
                    Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.example.alveera.parstagram", media);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                    // So as long as the result is not null, it's safe to use the intent.
                    if (intent.resolveActivity(ProfileFragment.this.getActivity().getPackageManager()) != null) {
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void uploadProfilePic(ParseFile imageFile, ParseUser imageUser) {
        imageUser.put("profPic", imageFile);
        imageUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("HomeActivity", "Create post success");
                }
                else {
                    e.printStackTrace();
                }
            }
        });
        profThumbnail.setVisibility(View.INVISIBLE);
        takePic.setVisibility(View.VISIBLE);
        submitPic.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), "Uploaded SUCCESSFULLY!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == -1) {
                photoFile = new File(imagePath);
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                profThumbnail.setImageBitmap(takenImage);

                profThumbnail.setVisibility(View.VISIBLE);
                takePic.setVisibility(View.INVISIBLE);
                submitPic.setVisibility(View.VISIBLE);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadTopProfPosts() {
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
                            if (objects.get(i).getUser().fetchIfNeeded().getUsername().equals(ParseUser.getCurrentUser().getUsername())) {
                                profPosts.add(0, objects.get(i));
                                //pAdapter.notifyItemInserted(0);
                                profPostAdapter.notifyItemInserted(0);
                            }

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                    }
                    pb.setVisibility(ProgressBar.INVISIBLE);
                }else {
                    e.printStackTrace();
                }
            }
        });
    }

}
