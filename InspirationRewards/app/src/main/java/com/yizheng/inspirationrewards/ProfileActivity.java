package com.yizheng.inspirationrewards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private TextView name, location, pointsAwarded, dept, pos, pointsToAward, story;
    private ImageView imageView;

    private Profile p;

    private int CODE_FOR_EDIT_ACTIVITY = 1;
    private int CODE_FOR_LEADERBOARD = 2;

    private RecyclerView recyclerView;
    private RewardAdapter rewardAdapter;

    private ArrayList<Reward> rewards = new ArrayList<>();

    private static final String TAG = "ProfileActivity";


    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Your Profile");

        recyclerView = findViewById(R.id.recycler_in_profile);
        rewardAdapter = new RewardAdapter(rewards);
        recyclerView.setAdapter(rewardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        name = findViewById(R.id.textView5);
        location = findViewById(R.id.textView6);
        pointsAwarded = findViewById(R.id.textView8);
        dept = findViewById(R.id.textView10);
        pos = findViewById(R.id.textView15);
        pointsToAward = findViewById(R.id.textView18);
        story = findViewById(R.id.textView21);
        imageView = findViewById(R.id.imageView);

        SharedPreferences sharedPreferences = getSharedPreferences("MY_CONTENT", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String n = sharedPreferences.getString("name", "");
        if (!n.isEmpty()) {
            editor.remove("name");
            editor.apply();
        }
        name.setText(n);

        String l = sharedPreferences.getString("location", "");
        if (!l.isEmpty()) {
            editor.remove("location");
            editor.apply();
        }
        location.setText(l);

        String pA = sharedPreferences.getString("pointsAwarded", "");
        if (!pA.isEmpty()) {
            editor.remove("pointsAwarded");
            editor.apply();
        }
        pointsAwarded.setText(pA);

        String d = sharedPreferences.getString("dept", "");
        if (!d.isEmpty()) {
            editor.remove("dept");
            editor.apply();
        }
        dept.setText(d);

        String po = sharedPreferences.getString("pos", "");
        if (!po.isEmpty()) {
            editor.remove("pos");
            editor.apply();
        }
        pos.setText(po);

        String pta = sharedPreferences.getString("pointsToAward", "");
        if (!pta.isEmpty()) {
            editor.remove("pointsToAward");
            editor.apply();
        }
        pointsToAward.setText(pta);

        String sto = sharedPreferences.getString("story", "");
        if (!sto.isEmpty()) {
            editor.remove("story");
            editor.apply();
        }
        story.setText(sto);




        Intent intent = getIntent();
        if (intent.hasExtra("createdProfile")) {
            p = (Profile) intent.getSerializableExtra("createdProfile");
            name.setText(p.getLastname()+", "+p.getFirstname()+" ("+p.getUsername()+")");
            location.setText(p.getLocation());
            pointsAwarded.setText(Integer.valueOf(p.totalPointsAwarded()).toString());
            dept.setText(p.getDepartment());
            pos.setText(p.getPosition());
            //pointsToAward.setText("1000");
            pointsToAward.setText(p.getPointsToAward().toString());
            story.setText(p.getStory());
            doConvert(p.getPhoto());
            makeCustomToast(this, "User create successful", Toast.LENGTH_LONG);
        }
        else if (intent.hasExtra("loginProfile")){
            p = (Profile) intent.getSerializableExtra("loginProfile");
            name.setText(p.getLastname()+", "+p.getFirstname()+" ("+p.getUsername()+")");
            location.setText(p.getLocation());
            pointsAwarded.setText(Integer.valueOf(p.totalPointsAwarded()).toString());//
            dept.setText(p.getDepartment());
            pos.setText(p.getPosition());
            pointsToAward.setText(p.getPointsToAward().toString());
            story.setText(p.getStory());
            doConvert(p.getPhoto());
            //rewards
            rewards.addAll(p.getRewards());

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == CODE_FOR_EDIT_ACTIVITY) {
            if (resultCode == RESULT_OK){
                if (data.hasExtra("editedProfile")){
                    p = (Profile) data.getSerializableExtra("editedProfile");
                    name.setText(p.getLastname()+", "+p.getFirstname()+" ("+p.getUsername()+")");
                    location.setText(p.getLocation());
                    pointsAwarded.setText(Integer.valueOf(p.totalPointsAwarded()).toString());     //?
                    dept.setText(p.getDepartment());
                    pos.setText(p.getPosition());
                    pointsToAward.setText(p.getPointsToAward().toString());
                    story.setText(p.getStory());
                    doConvert(p.getPhoto());
                    rewards.clear();
                    rewards.addAll(p.getRewards());
                    rewardAdapter.notifyDataSetChanged();
                }
            }
        }

        else {
            Log.d(TAG, "onActivityResult: Request Code "+requestCode);////////////////////////
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editMenu:
                Intent intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("profileToBeEdited", p);
                startActivityForResult(intent, CODE_FOR_EDIT_ACTIVITY);
                return true;
            case R.id.leaderboardMenu:
                String nameS = name.getText().toString();
                editor.putString("name", nameS);
                String locS = location.getText().toString();
                editor.putString("location", locS);
                String pointsAwaredS = pointsAwarded.getText().toString();
                editor.putString("pointsAwarded", pointsAwaredS);
                String deptS = dept.getText().toString();
                editor.putString("dept", deptS);
                String posS = pos.getText().toString();
                editor.putString("pos", posS);
                String pointsToAwardS = pointsToAward.getText().toString();
                editor.putString("pointsToAward", pointsToAwardS);
                String storyS = story.getText().toString();
                editor.putString("story", storyS);

                editor.apply();


                Intent intent1 = new Intent(this, LeaderboardActivity.class);
                intent1.putExtra("currentProfile", p);
                startActivityForResult(intent1, CODE_FOR_LEADERBOARD);//////////////////////////////
                //startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private void doConvert(String imgString) {

        byte[] imageBytes = Base64.decode(imgString, Base64.DEFAULT);
        //Log.d(TAG, "doConvert: Image byte array length: " + imgString.length());

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        //Log.d(TAG, "doConvert: Bitmap created from Base 64 text");

        imageView.setImageBitmap(bitmap);

    }
}
