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

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.data.MoviesContract;
import io.romo.popularmovies.model.Movie;

public class FavoriteMoviesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final int MOVIE_LOADER_ID = 0;

    @BindView(R.id.favorite_movies) RecyclerView favoriteMovies;
    FavoriteMoviesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite_movies, container, false);
        ButterKnife.bind(this, v);

        favoriteMovies.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        favoriteMovies.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        favoriteMovies.addItemDecoration(divider);

        adapter = new FavoriteMoviesAdapter();
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

                        int id = cursor.getInt(idIndex);
                        String title = cursor.getString(titleIndex);

                        Movie movie = new Movie();
                        movie.setId(id);
                        movie.setTitle(title);

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
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        adapter.replaceData(null);
    }

    private static class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesHolder> {

        private List<Movie> movieList;

        public FavoriteMoviesAdapter() {

        }

        public void replaceData(List<Movie> movieList) {
            this.movieList = movieList;
            notifyDataSetChanged();
        }

        @Override
        public FavoriteMoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_favorite_movie, parent, false);
            return new FavoriteMoviesHolder(v);
        }

        @Override
        public void onBindViewHolder(FavoriteMoviesHolder holder, int position) {
            Movie movie = movieList.get(position);
            holder.bind(movie);
        }

        @Override
        public int getItemCount() {
            return movieList == null ? 0 : movieList.size();
        }
    }

    static class FavoriteMoviesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView title;

        public FavoriteMoviesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Movie movie) {
            title.setText(movie.getTitle());
        }
    }
}
