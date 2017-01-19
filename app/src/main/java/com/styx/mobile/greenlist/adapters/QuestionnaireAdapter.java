package com.styx.mobile.greenlist.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.models.AdditionalParameter;
import com.styx.mobile.greenlist.models.Type;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

/**
 *
 */

public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder> {
    RealmList<AdditionalParameter> additionalParameters;
    ArrayList<String> arrayList;

    public QuestionnaireAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        additionalParameters = new RealmList<>();
        for (String question : arrayList) {
            additionalParameters.add(new AdditionalParameter(question));
        }
    }

    public RealmList<AdditionalParameter> getAdditionalParameters() {
        return additionalParameters;
    }

    @Override
    public QuestionnaireAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_answer, parent, false);
        return new QuestionnaireAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final QuestionnaireAdapter.ViewHolder holder, final int position) {
        holder.textViewQuestion.setText(arrayList.get(position));
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
                additionalParameters.get(position).setValue(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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
