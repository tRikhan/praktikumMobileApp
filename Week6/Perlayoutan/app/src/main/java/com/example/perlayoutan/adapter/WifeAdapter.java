package com.example.perlayoutan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.perlayoutan.R;
import com.example.perlayoutan.model.Wife;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class WifeAdapter extends ArrayAdapter<Wife> {
    private Context context;
    private ArrayList<Wife> wifeList;

    public WifeAdapter(Context context, ArrayList<Wife> wifeList) {
        super(context, R.layout.list_item_wife, wifeList);
        this.context = context;
        this.wifeList = wifeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_wife, parent, false);
            holder = new ViewHolder();
            holder.imageProfile = convertView.findViewById(R.id.imageProfile);
            holder.textName = convertView.findViewById(R.id.textName);
            holder.textDescription = convertView.findViewById(R.id.textDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Wife wife = wifeList.get(position);
        
        holder.imageProfile.setImageResource(wife.getProfileImageResId());
        holder.textName.setText(String.format("%s (%d)", wife.getName(), wife.getAge()));
        holder.textDescription.setText(String.format("From: %s\n%s", wife.getOrigin(), wife.getDescription()));

        return convertView;
    }

    private static class ViewHolder {
        ShapeableImageView imageProfile;
        TextView textName;
        TextView textDescription;
    }
}