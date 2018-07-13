package com.codepath.alveera.parstagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.alveera.parstagram.model.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PicFragment extends Fragment {


    private static final String imagePathTest = "/storage/emulated/0/DCIM/Camera/IMG_20180710_130908.jpg";
    private EditText descriptionInput;
    private Button createButton;
    private Button camButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_OK = 1;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    File photoFile;
    private String imagePath = "";
    ImageView ivPreview;

    ArrayList<Post> posts;
    InstaAdapter pAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        descriptionInput = (EditText) view.findViewById(R.id.description_et);
        createButton = (Button) view.findViewById(R.id.create_btn);
        camButton = (Button) view.findViewById(R.id.cam_btn);
        ivPreview = (ImageView) view.findViewById(R.id.ivPreview);
        camButton.setVisibility(View.VISIBLE);

        posts = new ArrayList<>();
        pAdapter = new InstaAdapter(posts);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                //photoFile = new File(imagePath);
                final ParseFile parseFile = new ParseFile(photoFile);

                createPost(description, parseFile, user);

            }
        });

        camButton.setOnClickListener(new View.OnClickListener() {
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
                    imagePath = media.getAbsolutePath(); // TODO absolute?
                    // Create a File reference to access to future access
                    //photoFile = getPhotoFileUri(imagePath);
                    // wrap File object into a content provider
                    // required for API >= 24
                    // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                    //media = getPhotoFileUri(photoFileName);
                    Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.example.alveera.parstagram", media);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                    // So as long as the result is not null, it's safe to use the intent.
                    if (intent.resolveActivity(PicFragment.this.getActivity().getPackageManager()) != null) {
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == -1) {
                photoFile = new File(imagePath);
                //String path = photoFile.getPath();
                //Bundle extras = data.getExtras();
                //Bitmap imageBitmap = (Bitmap) extras.get("data");
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPreview.setImageBitmap(takenImage);

                ivPreview.setVisibility(View.VISIBLE);
                camButton.setVisibility(View.INVISIBLE);
                descriptionInput.setVisibility(View.VISIBLE);
                createButton.setVisibility(View.VISIBLE);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createPost(String description, ParseFile imageFile, ParseUser imageUser) {
        Post newPost = Post.newInstance(imageUser, imageFile, description);
        ivPreview.setVisibility(View.INVISIBLE);
        camButton.setVisibility(View.VISIBLE);
        descriptionInput.setVisibility(View.INVISIBLE);
        descriptionInput.setText("");
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(descriptionInput.getWindowToken(), 0);
        createButton.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), "POSTED SUCCESSFULLY!", Toast.LENGTH_LONG).show();

        pAdapter.notifyItemInserted(0);
        //pAdapter.notifyItemInserted(posts.size() - 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pic, container, false);
    }
}
