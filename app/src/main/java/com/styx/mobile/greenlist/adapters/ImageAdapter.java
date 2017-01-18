package com.styx.mobile.greenlist.adapters;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.styx.mobile.greenlist.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by amal.george on 18-01-2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private ArrayList<String> imageList;
    private Context context;
    OnImageViewClickListener onImageViewClickListener;

    public ImageAdapter(Context context, OnImageViewClickListener onImageViewClickListener) {
        imageList = new ArrayList<>();
        imageList.add("");
        this.context = context;
        this.onImageViewClickListener = onImageViewClickListener;
    }

    public void addImage(String newImageLocation) {
        imageList.add(newImageLocation);
        notifyItemInserted(imageList.size());
    }

    public void addImage(String newImageLocation, int position) {
        imageList.add(position, newImageLocation);
        notifyItemInserted(position);
    }

    public void removeImage(int position) {
        imageList.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_image_thumb, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int index = position;
        if (!TextUtils.isEmpty(imageList.get(position)))
            Picasso.with(context).load(new File(imageList.get(position))).into(holder.imageThumb);
        holder.imageThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageViewClickListener.onImageViewClick(index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageThumb;

        ViewHolder(View itemView) {
            super(itemView);
            imageThumb = (ImageView) itemView.findViewById(R.id.imageThumb);
        }
    }

    public interface OnImageViewClickListener {
        void onImageViewClick(int position);
    }
}
