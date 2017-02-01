package com.styx.mobile.greenlist.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.models.Photo;
import com.styx.mobile.greenlist.ui.ListingDetailActivity;
import com.styx.mobile.greenlist.utils.Utils;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 *
 */

public class ListingSearchAdapter extends RealmRecyclerViewAdapter<Listing, ListingSearchAdapter.ViewHolder> {
    public ListingSearchAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Listing> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listing_min, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Listing thisListing = getItem(position);

        holder.editTextListingName.setText(thisListing.getTitle());
        holder.textViewLocation.setText(thisListing.getLocation().getName());
        holder.buttonCall.setText(thisListing.getContactNumber());

        /** Image List **/
        ArrayList<String> imageList = new ArrayList<>();
        for (Photo thisPhoto : thisListing.getPhotos()) {
            imageList.add(thisPhoto.getPath());
        }
        ImageAdapter imageAdapter = new ImageAdapter(context, new ImageAdapter.OnImageViewClickListener() {
            @Override
            public void onImageViewClick(int position) {
            }

            @Override
            public void onImageViewLongClick(int position) {
            }
        }, imageList);

        final long Id = thisListing.getId();
        holder.recyclerViewImageList.setAdapter(imageAdapter);
        holder.linearLayoutListingParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListingDetailActivity.class);
                intent.putExtra("parameterListingId", Id);
                Utils.startActivityWithClipReveal(intent, context, holder.linearLayoutListingParent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView editTextListingName;
        TextView textViewLocation;
        TextView buttonCall;
        RecyclerView recyclerViewImageList;
        LinearLayout linearLayoutListingParent;

        ViewHolder(View itemView) {
            super(itemView);
            editTextListingName = (TextView) itemView.findViewById(R.id.textViewListingName);
            textViewLocation = (TextView) itemView.findViewById(R.id.textViewLocation);
            buttonCall = (TextView) itemView.findViewById(R.id.buttonCall);
            linearLayoutListingParent = (LinearLayout) itemView.findViewById(R.id.linearLayoutListingParent);

            final LinearLayoutManager linearLayoutManagerImageList = new LinearLayoutManager(context);
            linearLayoutManagerImageList.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewImageList = (RecyclerView) itemView.findViewById(R.id.recyclerViewImageList);
            recyclerViewImageList.setHasFixedSize(true);
            recyclerViewImageList.setLayoutManager(linearLayoutManagerImageList);
        }
    }
}
