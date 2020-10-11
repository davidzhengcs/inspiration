package com.yizheng.inspirationrewards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RewardAdapter extends RecyclerView.Adapter<RewardViewHolder> {

    private ArrayList<Reward> rewards;

    public RewardAdapter(ArrayList<Reward> rewards) {
        this.rewards = rewards;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_item_profile, parent, false);

        return new RewardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        Reward r = rewards.get(position);
        holder.date.setText(r.getDate());
        holder.name.setText(r.getName());
        holder.points.setText(Integer.valueOf(r.getValue()).toString());
        holder.notes.setText(r.getNotes());
    }

    @Override
    public int getItemCount() {
        return rewards.size();
    }
}
