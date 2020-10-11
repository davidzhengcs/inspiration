package com.yizheng.inspirationrewards;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

    private LeaderboardActivity leaderboardActivity;
    private ArrayList<Profile> profiles;

    public ProfileAdapter(ArrayList<Profile> profiles, LeaderboardActivity leaderboardActivity) {
        this.leaderboardActivity = leaderboardActivity;
        this.profiles = profiles;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_item_leader, parent, false);
        itemView.setOnClickListener(leaderboardActivity);

        return new ProfileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile p = profiles.get(position);
        doConvert(p.getPhoto(), holder.imageView);
        holder.nameTxt.setText(p.getLastname()+", "+p.getFirstname());
        holder.posTxt.setText(p.getPosition()+", "+p.getDepartment());
        holder.pointsTxt.setText(new Integer(p.totalPointsAwarded()).toString());
        if (p.getUsername().equals(leaderboardActivity.getCurrentProfileUsername())){
            holder.nameTxt.setTextColor(Color.GREEN);
            holder.posTxt.setTextColor(Color.GREEN);
            holder.pointsTxt.setTextColor(Color.GREEN);
        }
        else {
            holder.nameTxt.setTextColor(Color.BLACK);
            holder.posTxt.setTextColor(Color.BLACK);
            holder.pointsTxt.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    private void doConvert(String imgString, ImageView imageView) {

        byte[] imageBytes = Base64.decode(imgString, Base64.DEFAULT);
        //Log.d(TAG, "doConvert: Image byte array length: " + imgString.length());

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        //Log.d(TAG, "doConvert: Bitmap created from Base 64 text");

        imageView.setImageBitmap(bitmap);

    }
}
