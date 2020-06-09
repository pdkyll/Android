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

import com.taehyungkim.project_a.R;

import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private ArrayList<Integer> movie_view;
    private ArrayList<String> movie_name;
    private ArrayList<String> book_rate;
    private ArrayList<String> age;
    private ArrayList<String> date;

    Button bt_movie_details;

    public MovieListAdapter(ArrayList<Integer> movie_view, ArrayList<String> movie_name,
                            ArrayList<String> book_rate, ArrayList<String> age, ArrayList<String> date) {
        this.movie_view = movie_view;
        this.movie_name = movie_name;
        this.book_rate = book_rate;
        this.age = age;
        this.date = date;
    }

    @NonNull
    @Override
    public MovieListAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_list, parent, false);
        bt_movie_details = (Button) view.findViewById(R.id.bt_movie_details);

        bt_movie_details.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_nav_movie_list_to_nav_movie_details);
        });

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MovieViewHolder holder, int position) {
        holder.movie_view.setImageResource(movie_view.get(position));
        holder.movie_name.setText(movie_name.get(position));
        holder.book_rate.setText(book_rate.get(position));
        holder.age.setText(age.get(position));
        holder.date.setText(date.get(position));

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
        private TextView date;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            movie_view = (ImageView) itemView.findViewById(R.id.movie_view);
            movie_name = (TextView) itemView.findViewById(R.id.movie_name_text);
            book_rate = (TextView) itemView.findViewById(R.id.book_rate_text);
            age = (TextView) itemView.findViewById(R.id.age_text);
            date = (TextView) itemView.findViewById(R.id.date_text);
        }
    }
}
