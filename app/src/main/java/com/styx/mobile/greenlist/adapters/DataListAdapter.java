package com.styx.mobile.greenlist.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.utils.Pair;

import java.util.ArrayList;


public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.ViewHolder> {
    private ArrayList<Pair<String>> thisDataList;

    public DataListAdapter(ArrayList<Pair<String>> dataList) {
        thisDataList = dataList;

    }

    @Override
    public DataListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_list, parent, false);
        return new DataListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataListAdapter.ViewHolder holder, final int position) {
        holder.textViewQuestion.setText(thisDataList.get(position).getKey());
        holder.textViewAnswer.setText(thisDataList.get(position).getValue());
        holder.textViewAnswer.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return thisDataList.size();
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
