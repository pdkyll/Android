package com.taehyungkim.project_a;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentItemRecyclerAdapter extends RecyclerView.Adapter<CommentItemRecyclerAdapter.ViewHolder> {
    private ArrayList<String> recCount;


    CommentItemRecyclerAdapter(ArrayList<String> recCount) {
        this.recCount = recCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commnet_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recCount.setText(recCount.get(position));
    }

    @Override
    public int getItemCount() {
        return recCount.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recCount = itemView.findViewById(R.id.recCount_text_view);
        }
    }
}
