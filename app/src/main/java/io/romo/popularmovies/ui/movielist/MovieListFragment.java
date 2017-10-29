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

package io.romo.popularmovies.ui.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.data.model.Movie;
import io.romo.popularmovies.data.remote.request.MovieService;
import io.romo.popularmovies.data.remote.request.ServiceGenerator;
import io.romo.popularmovies.data.remote.response.MovieResponse;
import io.romo.popularmovies.ui.moviedetail.MovieDetailActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.romo.popularmovies.ui.movielist.SortBy.FAVORITES;
import static io.romo.popularmovies.ui.movielist.SortBy.HIGHEST_RATED;
import static io.romo.popularmovies.ui.movielist.SortBy.MOST_POPULAR;

public class MovieListFragment extends Fragment
        implements MovieAdapter.ListItemClickListener {

    private static final String STATE_SORT_BY = "sort_by";

    @BindView(R.id.movies) RecyclerView movies;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_message) View errorMessage;
    private MovieAdapter adapter;

    // By default movies are sorted by most popular
    private SortBy sortBy = MOST_POPULAR;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);

        GridLayoutManager layoutManager =
                new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.num_columns));
        movies.setLayoutManager(layoutManager);

        movies.setHasFixedSize(true);

        adapter = new MovieAdapter(this);
        movies.setAdapter(adapter);

        if (savedInstanceState != null) {
            sortBy = (SortBy) savedInstanceState.getSerializable(STATE_SORT_BY);
        }

        loadMovies();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_SORT_BY, sortBy);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_movie_list, menu);

        SubMenu sortBySubMenu = menu.findItem(R.id.action_sort_by).getSubMenu();
        switch (sortBy) {
            case MOST_POPULAR:
                sortBySubMenu.findItem(R.id.most_popular).setChecked(true);
                break;
            case HIGHEST_RATED:
                sortBySubMenu.findItem(R.id.highest_rated).setChecked(true);
                break;
            case FAVORITES:
                sortBySubMenu.findItem(R.id.favorites).setChecked(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (item.getGroupId() == R.id.sort_by) {
            SortBy previousSortBy = sortBy;
    
            switch (itemId) {
                case R.id.most_popular:
                    sortBy = MOST_POPULAR;
                    break;
                case R.id.highest_rated:
                    sortBy = HIGHEST_RATED;
                    break;
                case R.id.favorites:
                    sortBy = FAVORITES;
                    break;
            }

            // Sorting order has not changed, therefore their is no need to reload movies
            if (sortBy == previousSortBy) {
                return true;
            }
            item.setChecked(true);

            loadMovies();
                
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(Movie movie) {
        Intent intent = MovieDetailActivity.newIntent(getActivity(), movie);
        startActivity(intent);
    }

    private void loadMovies() {
        // Not yet implemented
        if (sortBy == SortBy.FAVORITES) {
            return;
        }
    
        MovieService movieService = ServiceGenerator.createService(MovieService.class);
        Call<MovieResponse> call;
        if (sortBy == SortBy.MOST_POPULAR) {
            call = movieService.getPopularMovies();
        } else {
            call = movieService.getTopRatedMovies();
        }
        
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                showMovies();
                adapter.setMovieList(response.body().getResults());
                movies.getLayoutManager().scrollToPosition(0);
            }
    
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                showErrorMessage();
            }
        });
    }

    private void showMovies() {
        movies.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        movies.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }
}
