package com.styx.mobile.greenlist.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.models.Type;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;


public class TypeAdapter extends RealmRecyclerViewAdapter<Type, TypeAdapter.ViewHolder> {
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Type type);
    }

    public TypeAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Type> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public TypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type, parent, false);
        return new TypeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TypeAdapter.ViewHolder holder, int position) {
        holder.textViewName.setText(getItem(position).getName());
        Picasso.with(context).load(getItem(position).getIcon()).into(holder.imageViewIcon);
        final Type type = Realm.getDefaultInstance().copyFromRealm(getItem(position));
        if (onItemClickListener != null) {
            holder.linearLayoutType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(type);
                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewIcon;
        private TextView textViewName;
        private LinearLayout linearLayoutType;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayoutType = (LinearLayout) itemView.findViewById(R.id.linearLayoutType);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.imageViewIcon);
        }
    }
}


