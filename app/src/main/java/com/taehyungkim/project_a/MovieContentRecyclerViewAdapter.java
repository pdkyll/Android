package com.taehyungkim.project_a;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MovieContentRecyclerViewAdapter extends RecyclerView.Adapter<MovieContentRecyclerViewAdapter.ViewHolder> {

    private final List<MovieContent> movieContents;

    public MovieContentRecyclerViewAdapter(List<MovieContent> movieContents) {
        this.movieContents = movieContents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return movieContents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ListItemMovieContentBinding binding;

        public ViewHolder(ListItemMovieContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
