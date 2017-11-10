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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.romo.popularmovies.R;
import io.romo.popularmovies.model.MovieReview;
import io.romo.popularmovies.rest.service.TheMovieDbService;
import io.romo.popularmovies.rest.TheMovieDbClient;
import io.romo.popularmovies.rest.response.MovieReviewsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieReviewsFragment extends Fragment implements Callback<MovieReviewsResponse> {
    
    private static final String ARG_MOVIE_ID = "movie_id";

    private static final String SAVED_LIST_POSITION = "list_position";
    
    @BindView(R.id.movie_reviews) RecyclerView movieReviews;
    private MovieReviewsAdapter adapter;
    
    private int movieId;
    private Parcelable savedListPosition;
    
    public static MovieReviewsFragment newInstance(int movieId) {
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        
        MovieReviewsFragment fragment = new MovieReviewsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieId = getArguments().getInt(ARG_MOVIE_ID);
        if (savedInstanceState != null) {
            savedListPosition = savedInstanceState.getParcelable(SAVED_LIST_POSITION);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_reviews, container, false);
        ButterKnife.bind(this, v);
        
        movieReviews.setHasFixedSize(true);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        movieReviews.setLayoutManager(layoutManager);
        
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        movieReviews.addItemDecoration(divider);
        
        adapter = new MovieReviewsAdapter(itemListener);
        movieReviews.setAdapter(adapter);
    
        TheMovieDbClient.createService(TheMovieDbService.class)
                .getMovieReviews(movieId)
                .enqueue(this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_LIST_POSITION,
                movieReviews.getLayoutManager().onSaveInstanceState());
    }
    
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResponse(Call<MovieReviewsResponse> call, Response<MovieReviewsResponse> response) {
        if (response.isSuccessful()) {
            adapter.replaceData(response.body().getResults());
            movieReviews.getLayoutManager().onRestoreInstanceState(savedListPosition);
        } else {
            // TODO: Handle error
        }
    }
    
    @Override
    public void onFailure(Call<MovieReviewsResponse> call, Throwable t) {
        // TODO: Handle error
    }
    
    private MovieReviewItemListener itemListener = new MovieReviewItemListener() {
        @Override
        public void onMovieReviewClick(MovieReview clickedMovieReview) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(clickedMovieReview.getUrl()));
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    };
    
    private static class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsViewHolder> {
        
        private List<MovieReview> movieReviewList;
        private MovieReviewItemListener itemListener;
        
        public MovieReviewsAdapter(MovieReviewItemListener itemListener) {
            this.itemListener = itemListener;
        }
        
        public void replaceData(List<MovieReview> movieReviewList) {
            this.movieReviewList = movieReviewList;
            notifyDataSetChanged();
        }
        
        @Override
        public MovieReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_movie_review, parent, false);
            return new MovieReviewsViewHolder(v);
        }
        
        @Override
        public void onBindViewHolder(MovieReviewsViewHolder holder, int position) {
            MovieReview review = movieReviewList.get(position);
            holder.bind(review, itemListener);
        }
        
        @Override
        public int getItemCount() {
            return movieReviewList == null ? 0 : movieReviewList.size();
        }
    }
    
    static class MovieReviewsViewHolder extends RecyclerView.ViewHolder {
        
        @BindView(R.id.author) TextView author;
        @BindView(R.id.content) TextView content;
        @BindView(R.id.read_full_review) Button readFullReview;
        
        private MovieReview review;
        private MovieReviewItemListener itemListener;
        
        public MovieReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        
        public void bind(MovieReview review, MovieReviewItemListener itemListener) {
            this.review = review;
            this.itemListener = itemListener;
            
            author.setText(review.getAuthor());
            content.setText(review.getContent());
        }
        
        @OnClick(R.id.read_full_review)
        void readFullReviewOnClick() {
            itemListener.onMovieReviewClick(review);
        }
    }
    
    private interface MovieReviewItemListener {
        
        void onMovieReviewClick(MovieReview clickedMovieReview);
    }
}
