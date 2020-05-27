package com.taehyungkim.project_a.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyungkim.project_a.R;

import java.util.ArrayList;

public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.ViewHolder> {
    private ArrayList<String> id;
    private ArrayList<String> time;
    private ArrayList<Float> ratingBar;
    private ArrayList<String> comment;
    private ArrayList<String> recCount;

    public MovieDetailsAdapter(ArrayList<String> id, ArrayList<String> time,
                               ArrayList<Float> ratingBar, ArrayList<String> comment,
                               ArrayList<String> recCount) {
        this.id = id;
        this.time = time;
        this.ratingBar = ratingBar;
        this.comment = comment;
        this.recCount = recCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id.setText(id.get(position));
        holder.time.setText(time.get(position));
        holder.ratingBar.setRating(ratingBar.get(position));
        holder.comment.setText(comment.get(position));
        holder.recCount.setText(recCount.get(position));
    }

    @Override
    public int getItemCount() {
        return recCount.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView time;
        RatingBar ratingBar;
        TextView comment;
        TextView recCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.user_id_view);
            time = itemView.findViewById(R.id.writingTime_text_view);
            ratingBar = itemView.findViewById(R.id.comment_rating_view);
            comment = itemView.findViewById(R.id.comment_text_view);
            recCount = itemView.findViewById(R.id.recCount_text_view);
        }
    }
}
