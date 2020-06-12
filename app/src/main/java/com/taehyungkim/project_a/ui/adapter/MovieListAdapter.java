package com.taehyungkim.project_a.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.taehyungkim.project_a.R;

import java.util.ArrayList;
import java.util.Locale;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private ArrayList<String> movie_view;
    private ArrayList<String> movie_name;
    private ArrayList<Float> book_rate;
    private ArrayList<Integer> age;

    public MovieListAdapter(ArrayList<String> movie_view, ArrayList<String> movie_name,
                            ArrayList<Float> book_rate, ArrayList<Integer> age) {
        this.movie_view = movie_view;
        this.movie_name = movie_name;
        this.book_rate = book_rate;
        this.age = age;
    }

    @NonNull
    @Override
    public MovieListAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_list, parent, false);
        Button bt_movie_details = (Button) view.findViewById(R.id.bt_movie_details);

        bt_movie_details.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_nav_movie_list_to_nav_movie_details)
        );

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MovieViewHolder holder, int position) {
        Glide.with(holder.itemView).load(movie_view.get(position)).
                override(holder.movie_view.getWidth(), holder.movie_view.getHeight()).into(holder.movie_view);
        holder.movie_name.setText(String.format(Locale.US, "%d. %s", (position + 1), movie_name.get(position)));
        holder.book_rate.setText(String.format(Locale.US, "%s%%", book_rate.get(position).toString()));
        holder.age.setText(String.format(Locale.US, "%s세 관람가", age.get(position).toString()));

    }

    @Override
    public int getItemCount() {
        return movie_view.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView movie_view;
        private TextView movie_name;
        private TextView book_rate;
        private TextView age;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            movie_view = (ImageView) itemView.findViewById(R.id.movie_view);
            movie_name = (TextView) itemView.findViewById(R.id.movie_name_text);
            book_rate = (TextView) itemView.findViewById(R.id.book_rate_text);
            age = (TextView) itemView.findViewById(R.id.age_text);
        }
    }
}
