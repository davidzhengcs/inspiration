package com.yizheng.inspirationrewards;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameTxt, posTxt, pointsTxt;

    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView6);
        nameTxt = itemView.findViewById(R.id.textView19);
        posTxt = itemView.findViewById(R.id.textView23);
        pointsTxt = itemView.findViewById(R.id.textView24);
    }
}
