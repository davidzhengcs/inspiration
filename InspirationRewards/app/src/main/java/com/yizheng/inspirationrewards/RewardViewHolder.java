package com.yizheng.inspirationrewards;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RewardViewHolder extends RecyclerView.ViewHolder {

    TextView date, name, points, notes;

    public RewardViewHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.textView12);
        name = itemView.findViewById(R.id.textView13);
        points = itemView.findViewById(R.id.textView14);
        notes = itemView.findViewById(R.id.textView17);
    }
}
