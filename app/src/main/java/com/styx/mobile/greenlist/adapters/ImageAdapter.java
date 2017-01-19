package com.styx.mobile.greenlist.adapters;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.styx.mobile.greenlist.R;

import java.io.File;
import java.util.ArrayList;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private ArrayList<String> imageList;
    private Context context;
    private OnImageViewClickListener onImageViewClickListener;
    public final static String LIST_EMPTY_IMAGE = "LIST_EMPTY";

    public ImageAdapter(Context context, OnImageViewClickListener onImageViewClickListener) {
        imageList = new ArrayList<>();
        imageList.add(LIST_EMPTY_IMAGE);
        this.context = context;
        this.onImageViewClickListener = onImageViewClickListener;
    }

    public void addImage(String newImageLocation) {
        if (imageList.get(0).equals(LIST_EMPTY_IMAGE)) {
            imageList.remove(0);
            notifyItemRemoved(0);
        }
        imageList.add(newImageLocation);
        notifyItemInserted(imageList.size());
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.imageThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageViewClickListener.onImageViewClick(holder.getAdapterPosition());
            }
        });
        holder.imageThumb.setOnLongClickListener(new View.OnLongClickListener() {
                                                     @Override
                                                     public boolean onLongClick(View v) {
                                                         if (imageList.size() > 1)
                                                             removeImage(holder.getAdapterPosition());
                                                         return true;
                                                     }
                                                 }

        );
        if (!imageList.get(holder.getAdapterPosition()).equals(LIST_EMPTY_IMAGE))
            Picasso.with(context).load(new File(imageList.get(position))).into(holder.imageThumb);
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
