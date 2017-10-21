package io.romo.popularmovies.movielist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import io.romo.popularmovies.R;
import io.romo.popularmovies.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movieList, ListItemClickListener listItemClickListener) {
        this.movieList = movieList;
        this.listItemClickListener = listItemClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.bindMovie(movie);
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ImageView moviePoster;
        private Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            moviePoster = itemView.findViewById(R.id.movie_poster);
        }

        public void bindMovie(Movie movie) {
            this.movie = movie;
            // TODO Load movie poster
        }

        @Override
        public void onClick(View view) {
            listItemClickListener.onListItemClick(movie);
        }
    }
}
