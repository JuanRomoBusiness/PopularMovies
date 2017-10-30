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

import android.content.Context;
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
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.data.model.Movie;
import io.romo.popularmovies.data.remote.request.MovieService;
import io.romo.popularmovies.data.remote.request.ServiceGenerator;
import io.romo.popularmovies.data.remote.response.MovieResponse;
import io.romo.popularmovies.ui.moviedetail.MovieDetailActivity;
import io.romo.popularmovies.util.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.romo.popularmovies.ui.movielist.SortBy.FAVORITES;
import static io.romo.popularmovies.ui.movielist.SortBy.HIGHEST_RATED;
import static io.romo.popularmovies.ui.movielist.SortBy.MOST_POPULAR;

public class MoviesFragment extends Fragment implements Callback<MovieResponse> {
    
    private static final String STATE_SORT_BY = "sort_by";
    
    @BindView(R.id.movies) RecyclerView movies;
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
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);
    
        movies.setHasFixedSize(true);
        
        GridLayoutManager layoutManager =
                new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.num_columns));
        movies.setLayoutManager(layoutManager);
        
        adapter = new MovieAdapter(itemListener);
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
    
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
        if (response.isSuccessful()) {
            adapter.replaceData(response.body().getResults());
            
            movies.getLayoutManager().scrollToPosition(0);
        } else {
            // TODO: Handle error
        }
    }
    
    @Override
    public void onFailure(Call<MovieResponse> call, Throwable t) {
        // TODO: Handle error
    }
    
    private void loadMovies() {
        Call<MovieResponse> call = null;
    
        MovieService service = ServiceGenerator.createService(MovieService.class);
    
        switch (sortBy) {
            case MOST_POPULAR:
                call = service.getPopularMovies();
                break;
            case HIGHEST_RATED:
                call = service.getTopRatedMovies();
                break;
            case FAVORITES:
                // TODO: Implement favorites
                break;
        }
    
        if (call != null) {
            call.enqueue(this);
        }
    }
    
    private MovieItemListener itemListener = new MovieItemListener() {
        @Override
        public void onMovieClick(Movie clickedMovie) {
            Intent intent = MovieDetailActivity.newIntent(getActivity(), clickedMovie);
            startActivity(intent);
        }
    };
    
    static class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    
        private List<Movie> movieList;
        private MovieItemListener itemListener;
    
        public MovieAdapter(MovieItemListener itemListener) {
            this.itemListener = itemListener;
        }
    
        public void replaceData(List<Movie> movieList) {
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
            holder.bindMovie(movie, itemListener);
        }
    
        @Override
        public int getItemCount() {
            return movieList == null ? 0 : movieList.size();
        }
    }
    
    static class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        
        @BindView(R.id.movie_poster) ImageView moviePoster;
        
        private Movie movie;
        private MovieItemListener itemListener;
        
        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            
            itemView.setOnClickListener(this);
        }
        
        public void bindMovie(Movie movie, MovieItemListener itemListener) {
            this.movie = movie;
            this.itemListener = itemListener;
            
            Context context = moviePoster.getContext();
            Picasso.with(context).load(NetworkUtils.createImageUrl(movie.getPosterPath(),
                                                                   NetworkUtils.ImageSize.SMALL))
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
