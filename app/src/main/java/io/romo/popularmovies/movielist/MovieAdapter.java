package io.romo.popularmovies.movielist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Movie movie);
    }

    public MovieAdapter(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
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

        @BindView(R.id.movie_poster) ImageView moviePoster;
        private Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bindMovie(Movie movie) {
            this.movie = movie;
            Context context = moviePoster.getContext();
            Picasso.with(context).load(movie.getPosterPath())
                    .placeholder(R.drawable.place_holder_w300)
                    .into(moviePoster);
        }

        @Override
        public void onClick(View view) {
            listItemClickListener.onListItemClick(movie);
        }
    }
}