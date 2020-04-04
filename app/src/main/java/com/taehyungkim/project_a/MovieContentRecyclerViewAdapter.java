package com.taehyungkim.project_a;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyungkim.project_a.databinding.ListItemMoviecontentBinding;

import java.util.List;

public class MovieContentRecyclerViewAdapter extends RecyclerView.Adapter<MovieContentRecyclerViewAdapter.ViewHolder> {

    private final List<MovieContent> mMovieContents;

    public MovieContentRecyclerViewAdapter(List<MovieContent> movieContents) {
        mMovieContents = movieContents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ListItemMoviecontentBinding binding = ListItemMoviecontentBinding.
                                                inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieContent movieContent = mMovieContents.get(position);
        holder.binding.setMoviecontent(movieContent);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mMovieContents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ListItemMoviecontentBinding binding;

        public ViewHolder(ListItemMoviecontentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
