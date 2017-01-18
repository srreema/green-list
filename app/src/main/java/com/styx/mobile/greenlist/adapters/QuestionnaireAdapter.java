package com.styx.mobile.greenlist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;

/**
 *
 */

public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder> {
    public QuestionnaireAdapter(){

    }
    @Override
    public QuestionnaireAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_answer, parent, false);
        return new QuestionnaireAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuestionnaireAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion, textViewAnswer;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewQuestion = (TextView) itemView.findViewById(R.id.textViewQuestion);
            textViewAnswer = (TextView) itemView.findViewById(R.id.textViewAnswer);
        }
    }
}
