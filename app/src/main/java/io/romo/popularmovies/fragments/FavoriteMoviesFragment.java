/*
 * Copyright 2017 Juan Romo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.romo.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.activities.MovieDetailsActivity;
import io.romo.popularmovies.data.MoviesContract;
import io.romo.popularmovies.model.Movie;

public class FavoriteMoviesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final int MOVIE_LOADER_ID = 0;

    private static final String SAVED_LIST_POSITION = "list_position";

    @BindView(R.id.favorite_movies) RecyclerView favoriteMovies;
    private MoviesAdapter adapter;

    private Parcelable savedListPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            savedListPosition = savedInstanceState.getParcelable(SAVED_LIST_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite_movies, container, false);
        ButterKnife.bind(this, v);

        favoriteMovies.setHasFixedSize(true);

        GridLayoutManager layoutManager =
                new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.num_columns));
        favoriteMovies.setLayoutManager(layoutManager);

        adapter = new MoviesAdapter(itemListener);
        favoriteMovies.setAdapter(adapter);

        getActivity().getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_LIST_POSITION,
                favoriteMovies.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<List<Movie>>(getActivity()) {

            List<Movie> movieData = null;

            @Override
            protected void onStartLoading() {
                if (movieData != null) {
                    deliverResult(movieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {

                try {
                    Cursor cursor = getActivity()
                            .getContentResolver()
                            .query(MoviesContract.MovieEntry.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    MoviesContract.MovieEntry.COLUMN_TITLE);

                    List<Movie> movieList = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        int idIndex = cursor.getColumnIndex(MoviesContract.MovieEntry._ID);
                        int titleIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE);
                        int releaseDateIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE);
                        int voteAverageIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                        int voteCountIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_COUNT);
                        int overviewIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW);
                        int posterPathIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH);
                        int backdropPathIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH);

                        int id = cursor.getInt(idIndex);
                        String title = cursor.getString(titleIndex);
                        String releaseDate = cursor.getString(releaseDateIndex);
                        double voteAverage = cursor.getDouble(voteAverageIndex);
                        int voteCount = cursor.getInt(voteCountIndex);
                        String overview = cursor.getString(overviewIndex);
                        String posterPath = cursor.getString(posterPathIndex);
                        String backdropPath = cursor.getString(backdropPathIndex);

                        Movie movie = new Movie(id, title, releaseDate, voteAverage,
                                voteCount, overview, posterPath, backdropPath);

                        movieList.add(movie);
                    }
                    cursor.close();

                    return movieList;


                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(List<Movie> data) {
                movieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        adapter.replaceData(data);
        favoriteMovies.getLayoutManager().onRestoreInstanceState(savedListPosition);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        adapter.replaceData(null);
    }

    private MovieItemListener itemListener = new MovieItemListener() {
        @Override
        public void onMovieClick(Movie clickedMovie) {
            Intent intent = MovieDetailsActivity.newIntent(getActivity(), clickedMovie);
            startActivity(intent);
        }
    };

    static class MoviesAdapter extends RecyclerView.Adapter<MoviesViewHolder> {

        private List<Movie> movieList;
        private MovieItemListener itemListener;

        public MoviesAdapter(MovieItemListener itemListener) {
            this.itemListener = itemListener;
        }

        public void replaceData(List<Movie> movieList) {
            this.movieList = movieList;
            notifyDataSetChanged();
        }

        @Override
        public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_movie, parent, false);
            return new MoviesViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MoviesViewHolder holder, int position) {
            Movie movie = movieList.get(position);
            holder.bindMovie(movie, itemListener);
        }

        @Override
        public int getItemCount() {
            return movieList == null ? 0 : movieList.size();
        }
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.movie_poster)
        ImageView moviePoster;

        private Movie movie;
        private MovieItemListener itemListener;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bindMovie(Movie movie, MovieItemListener itemListener) {
            this.movie = movie;
            this.itemListener = itemListener;

            Context context = moviePoster.getContext();
            Picasso.with(context).load("https://image.tmdb.org/t/p/w300" + movie.getPosterPath())
                    .placeholder(R.drawable.poster_place_holder_w300)
                    .into(moviePoster);
        }

        @Override
        public void onClick(View view) {
            itemListener.onMovieClick(movie);
        }
    }

    private interface MovieItemListener {

        void onMovieClick(Movie clickedMovie);
    }
}
