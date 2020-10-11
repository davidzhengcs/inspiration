package com.yizheng.inspirationrewards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Profile currentProfile;
    private ProfileAdapter profileAdapter;

    private int CODE_FOR_AWARD_ACTIVITY = 3;

    private ArrayList<Profile> ps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadboard);

        recyclerView = findViewById(R.id.recycler_leader);
        profileAdapter = new ProfileAdapter(ps, this);
        recyclerView.setAdapter(profileAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CommonInfo.setupHomeIndicator(getSupportActionBar());

        Intent intent = getIntent();
        if (intent.hasExtra("currentProfile")){
            currentProfile = (Profile) intent.getSerializableExtra("currentProfile");
            String username = currentProfile.getUsername();
            String password = currentProfile.getPassword();
            new LeaderboardAsyncTask(this).execute(username, password);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        new LeaderboardAsyncTask(this).execute(currentProfile.getUsername(), currentProfile.getPassword());
    }

    public void processResult(ArrayList<Profile> profiles){

        ps.clear();
        ps.addAll(profiles);
        Collections.sort(ps);
        profileAdapter.notifyDataSetChanged();
    }

    public String getCurrentProfileUsername(){
        return currentProfile.getUsername();
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildLayoutPosition(v);
        Profile p = ps.get(position);
        if (p.getUsername().equals(currentProfile.getUsername())){
            return;
        }
        Intent intent = new Intent(this, AwardActivity.class);
        intent.putExtra("receiver", p);
        intent.putExtra("sender", currentProfile);
        startActivityForResult(intent, CODE_FOR_AWARD_ACTIVITY);
    }
}
