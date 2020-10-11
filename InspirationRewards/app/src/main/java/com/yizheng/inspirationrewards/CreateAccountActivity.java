package com.yizheng.inspirationrewards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class CreateAccountActivity extends AppCompatActivity {

    private ImageView imageView;
    private File currentImageFile;
    private int REQUEST_IMAGE_GALLERY = 1;
    private int REQUEST_IMAGE_CAPTURE = 2;
    private final String studentid = "1427953";
    private final String baseUrl = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private EditText username, password, fname, lname, department, position, story;
    private CheckBox isAdministrator;
//    private FusedLocationProviderClient mFusedLocationClient;
//    private static final int LOCATION_REQUEST = 111;

//    private static String locationString = "Unspecified Location";

    private boolean isCreation;

    private Profile p;

    private static int MAX_LEN = 360;

    private static int INITIAL_POINTS_TO_AWARD = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        imageView = findViewById(R.id.photoImageView);
        setTitle("Create Profile");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        username = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        fname = findViewById(R.id.editText3);
        lname = findViewById(R.id.editText4);
        department = findViewById(R.id.editText5);
        position = findViewById(R.id.editText6);
        story = findViewById(R.id.editText7);
        isAdministrator = findViewById(R.id.checkBox2);

        CommonInfo.setupHomeIndicator(getSupportActionBar());
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        determineLocation();

        setupTextView();

        Intent intent = getIntent();
        if (intent.hasExtra("profileToBeEdited")) {
            p = (Profile) intent.getSerializableExtra("profileToBeEdited");
            username.setText(p.getUsername());
            password.setText(p.getPassword());
            isAdministrator.setChecked(p.isAdministrator());
            fname.setText(p.getFirstname());
            lname.setText(p.getLastname());
            department.setText(p.getDepartment());
            position.setText(p.getPosition());
            story.setText(p.getStory());
            doConvert(p.getPhoto());
            isCreation = false;
            setTitle("Edit Profile");
        } else {
            isCreation = true;
        }
    }

    private void setupTextView() {
        EditText editText = findViewById(R.id.editText7);
        editText.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(MAX_LEN)
        });

        editText.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                        int len = s.toString().length();
                        String countText = "Your story: (" + len + " of " + MAX_LEN + ")";
                        ((TextView) findViewById(R.id.textView4)).setText(countText);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                    }
                });
    }

    private void doConvert(String imgString) {

        byte[] imageBytes = Base64.decode(imgString, Base64.DEFAULT);
        //Log.d(TAG, "doConvert: Image byte array length: " + imgString.length());

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        //Log.d(TAG, "doConvert: Bitmap created from Base 64 text");

        imageView.setImageBitmap(bitmap);

    }

    public void processResult(String msg) {
        if (msg.startsWith("Error")) {
            makeCustomToast(this, msg, Toast.LENGTH_LONG);
            return;
        }
        if (isCreation) {
            //p = new Profile();//don't need this
            try {
                JSONObject jsonObject = new JSONObject(msg);
                p.setFirstname(jsonObject.getString("firstName"));
                p.setLastname(jsonObject.getString("lastName"));
                p.setUsername(jsonObject.getString("username"));
                p.setDepartment(jsonObject.getString("department"));
                p.setStory(jsonObject.getString("story"));
                p.setPosition(jsonObject.getString("position"));
                p.setPassword(jsonObject.getString("password"));
                p.setPointsToAward(jsonObject.getInt("pointsToAward"));
                p.setAdministrator(jsonObject.getBoolean("admin"));
                p.setPhoto(jsonObject.getString("imageBytes"));
                p.setLocation(jsonObject.getString("location"));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            //if (isCreation) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("createdProfile", p);
            startActivity(intent);

        }
        else {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("editedProfile", p);
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
    }

    private void makeCustomToast(Context context, String message, int time) {
        Toast toast = Toast.makeText(context, message, time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView tv = toast.getView().findViewById(android.R.id.message);
        tv.setPadding(50, 25, 50, 25);
        tv.setTextColor(Color.WHITE);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.logo);

        builder.setPositiveButton("OK", (dialog, id) -> {
            createUpdateProfile();
        });

        builder.setNegativeButton("CANCEL", (dialog, id) -> {
        });

        builder.setTitle("Save Changes?");

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    private void createUpdateProfile() {
        if (isCreation) {
            p = new Profile();
            p.setPointsToAward(INITIAL_POINTS_TO_AWARD);
        }
        if (username.getText().toString() == "" || password.getText().toString() == "") {
            Toast.makeText(this, "Username or password cannot be empty.", Toast.LENGTH_LONG);
            return;
        }
        p.setUsername(username.getText().toString());
        p.setPassword(password.getText().toString());
        p.setFirstname(fname.getText().toString());
        p.setLastname(lname.getText().toString());
        p.setDepartment(department.getText().toString());
        p.setPosition(position.getText().toString());
        p.setStory(story.getText().toString());
        p.setPhoto(toBase64());
        //p.setNewUser(true);
        p.setAdministrator(isAdministrator.isChecked());
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //determineLocation();
        p.setLocation(MainActivity.getLocationString());
        new CreateAsyncTask(this, isCreation).execute(p);
    }

    private String toBase64() {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        String imageString64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return imageString64;

    }

    public void addPhotoClicked(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.logo);

        builder.setPositiveButton("CAMERA", (dialog, id) -> {
            doCamera();
        });

        builder.setNegativeButton("GALLERY", (dialog, id) -> {
            doGallery();
        });

        builder.setNeutralButton("CANCEL", (dialog, id) -> {
        });

        builder.setMessage("Take picture from:");
        builder.setTitle("Profile Picture");

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#ff9933"));
    }

    private void doCamera() {
        currentImageFile = new File(getExternalCacheDir(), "appimage_" + System.currentTimeMillis() + ".jpg");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        //Toast.makeText(this, "camera", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            try {
                processGallery(data);
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                processCamera();
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void processCamera() {
        Uri selectedImage = Uri.fromFile(currentImageFile);
        imageView.setImageURI(selectedImage);
    }

    private void processGallery(Intent data) {
        Uri galleryImageUri = data.getData();
        if (galleryImageUri == null)
            return;

        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(galleryImageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        imageView.setImageBitmap(selectedImage);

    }

    private void doGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY);
        //Toast.makeText(this, "gallery", Toast.LENGTH_SHORT).show();
    }
}
