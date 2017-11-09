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
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import io.romo.popularmovies.activities.MovieDetailsActivity;
import io.romo.popularmovies.model.Movie;
import io.romo.popularmovies.rest.TheMovieDbClient;
import io.romo.popularmovies.rest.response.MoviesResponse;
import io.romo.popularmovies.rest.service.TheMovieDbService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesFragment extends Fragment implements Callback<MoviesResponse> {

    public enum SortBy {POPULAR, TOP_RATED}

    private static final String SORT_BY = "sort_by";

    private static final String SAVED_LIST_POSITION = "list_position";

    @BindView(R.id.movies)
    RecyclerView movies;
    private MoviesAdapter adapter;

    private SortBy sortBy;
    private Parcelable savedListPosition;

    public static MoviesFragment newInstance(String sortBy) {
        Bundle args = new Bundle();
        args.putString(SORT_BY, sortBy);

        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortBy = SortBy.valueOf(getArguments().getString(SORT_BY));
        if (savedInstanceState != null) {
            savedListPosition = savedInstanceState.getParcelable(SAVED_LIST_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);

        movies.setHasFixedSize(true);

        GridLayoutManager layoutManager =
                new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.num_columns));
        movies.setLayoutManager(layoutManager);

        adapter = new MoviesAdapter(itemListener);
        movies.setAdapter(adapter);

        TheMovieDbService service = TheMovieDbClient.createService(TheMovieDbService.class);
        switch (sortBy) {
            case POPULAR:
                service.getPopularMovies()
                        .enqueue(this);
                break;
            case TOP_RATED:
                service.getTopRatedMovies()
                        .enqueue(this);
                break;
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_LIST_POSITION,
                movies.getLayoutManager().onSaveInstanceState());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
        if (response.isSuccessful()) {
            adapter.replaceData(response.body().getResults());
            movies.getLayoutManager().onRestoreInstanceState(savedListPosition);
        } else {
            // TODO: Handle error
        }
    }

    @Override
    public void onFailure(Call<MoviesResponse> call, Throwable t) {
        // TODO: Handle error
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
