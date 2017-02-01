package com.styx.mobile.greenlist.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.models.AdditionalParameter;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.utils.Pair;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


public class DataListAdapter extends RealmRecyclerViewAdapter<AdditionalParameter, DataListAdapter.ViewHolder> {

    public DataListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<AdditionalParameter> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    @Override
    public DataListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_list, parent, false);
        return new DataListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataListAdapter.ViewHolder holder, final int position) {
        holder.textViewQuestion.setText(getItem(position).getParameter().getName());
        holder.textViewAnswer.setText(getItem(position).getValue());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion, textViewAnswer;

        ViewHolder(View itemView) {
            super(itemView);
            textViewQuestion = (TextView) itemView.findViewById(R.id.textViewQuestion);
            textViewAnswer = (TextView) itemView.findViewById(R.id.textViewAnswer);
        }
    }
}


