package com.yizheng.inspirationrewards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.yizheng.inspirationrewards.CommonInfo.makeCustomToast;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private CheckBox checkBox;
    private SharedPreferences sharedPreferences;

    private static String locationString = "Unspecified Location";
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;


    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Rewards");
        sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        CommonInfo.setupLogo(getSupportActionBar());

        progressBar = findViewById(R.id.progressBar);

        checkBox = findViewById(R.id.checkBox);
        checkBox.setChecked(true);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        usernameEditText.setText(username);
        passwordEditText.setText(password);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        determineLocation();



    }


    private void determineLocation() {
        if (checkPermission()) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        locationString = getPlace(location);
                    }
                }
            });
        }
    }

    public static String getLocationString() {
        return locationString;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        determineLocation();
                    }
//                    else {
//                        textView.setText(R.string.deniedText);
//                    }
                }
            }
        }
    }

    private String getPlace(Location loc) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            return city + ", " + state;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return false;
        }
        return true;
    }

    public void processResult(String msg){
        if (msg.startsWith("Error")) {
            makeCustomToast(this, msg, Toast.LENGTH_LONG);
        }
        else {
            Profile p = new Profile();
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
                //p.setLocation(jsonObject.getString("location"));
                p.setLocation(locationString);

                JSONArray rewardArr = jsonObject.getJSONArray("rewards");
                if (rewardArr != null) {
                    for (int i = 0; i < rewardArr.length(); i++) {
                        Reward reward = new Reward();
                        reward.setUsername(rewardArr.getJSONObject(i).getString("username"));       /////
                        reward.setName(rewardArr.getJSONObject(i).getString("name"));
                        reward.setDate(rewardArr.getJSONObject(i).getString("date"));
                        reward.setNotes(rewardArr.getJSONObject(i).getString("notes"));
                        reward.setValue(rewardArr.getJSONObject(i).getInt("value"));
                        p.addReward(reward);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }



            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("loginProfile", p);
            startActivity(intent);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void loginClicked(View v){

        progressBar.setVisibility(View.VISIBLE);

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (checkBox.isChecked()){
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putString("username", username);
            prefsEditor.putString("password", password);
            prefsEditor.apply();
        }
//        String username = usernameEditText.getText().toString();
//        String password = passwordEditText.getText().toString();
        new LoginAsyncTask(this).execute(username, password);
    }

    public void tappedToCreateAccount(View v){
//        String username = usernameEditText.getText().toString();
//        String password = passwordEditText.getText().toString();
//        if (checkBox.isChecked()){
//            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
//            prefsEditor.putString("username", username);
//            prefsEditor.putString("password", password);
//            prefsEditor.apply();
//        }
        Intent intent = new Intent(this, CreateAccountActivity.class);
//        intent.putExtra("username", username);
//        intent.putExtra("password", password);
        startActivity(intent);
    }
}
