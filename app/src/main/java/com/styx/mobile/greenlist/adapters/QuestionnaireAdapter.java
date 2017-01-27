package com.styx.mobile.greenlist.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.utils.Pair;

import java.util.ArrayList;


public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder> {
    private ArrayList<Pair<String>> thisQuestionnaire;

    public QuestionnaireAdapter(ArrayList<Pair<String>> pairQuestionAnswer) {
        thisQuestionnaire = pairQuestionAnswer;
    }

    public ArrayList<Pair<String>> getThisQuestionnaire() {
        return thisQuestionnaire;
    }

    @Override
    public QuestionnaireAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_answer, parent, false);
        return new QuestionnaireAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final QuestionnaireAdapter.ViewHolder holder, final int position) {
        holder.textViewQuestion.setText(thisQuestionnaire.get(position).getKey());
        holder.textViewAnswer.setText(thisQuestionnaire.get(position).getValue());
        holder.textViewAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int position = holder.getAdapterPosition();
                thisQuestionnaire.get(position).setValue(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return thisQuestionnaire.size();
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
