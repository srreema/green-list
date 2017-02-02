package com.styx.mobile.greenlist.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.models.Parameter;
import com.styx.mobile.greenlist.utils.Pair;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;


public class ParameterAdapter extends RecyclerView.Adapter<ParameterAdapter.ViewHolder> {
    private List<Parameter> parameterList;
    private boolean isModified = false;
    OnEntryEditedListener onEntryEditedListener;

    public interface OnEntryEditedListener {
        void onEntryEdited(boolean isEdited);
    }

    public boolean isModified() {
        return isModified;
    }

    private void onModify() {
        if (!isModified && onEntryEditedListener != null) {
            isModified = true;
            onEntryEditedListener.onEntryEdited(isModified);
        }
    }

    public void setOnEntryEditedListener(OnEntryEditedListener onEntryEditedListener) {
        this.onEntryEditedListener = onEntryEditedListener;
    }

    public ParameterAdapter(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }

    @Override
    public ParameterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parameter, parent, false);
        return new ParameterAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ParameterAdapter.ViewHolder holder, final int position) {
        holder.editTextParameter.setText(parameterList.get(position).getName());
        holder.editTextParameter.selectAll();
        holder.editTextParameter.requestFocus();
        holder.editTextParameter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int position = holder.getAdapterPosition();
                parameterList.set(position, new Parameter(s.toString()));
                onModify();
            }
        });
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(holder.getAdapterPosition());
                onModify();
            }
        });
    }

    @Override
    public int getItemCount() {
        return parameterList.size();
    }

    public void add() {
        if (parameterList.isEmpty() || !TextUtils.isEmpty(parameterList.get(parameterList.size() - 1).getName())) {
            parameterList.add(new Parameter());
            notifyItemInserted(parameterList.size());
            onModify();
        }
    }

    public void remove(int position) {
        if (parameterList.size() > 1) {
            parameterList.remove(position);
            notifyItemRemoved(position);
            onModify();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        EditText editTextParameter;
        ImageView imageViewDelete;

        ViewHolder(View itemView) {
            super(itemView);
            editTextParameter = (EditText) itemView.findViewById(R.id.editTextParameter);
            imageViewDelete = (ImageView) itemView.findViewById(R.id.imageViewDelete);
        }
    }
}
