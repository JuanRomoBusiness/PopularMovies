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

package io.romo.popularmovies.ui.moviedetail;

import android.os.Bundle;
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
import io.romo.popularmovies.data.model.MovieReview;
import io.romo.popularmovies.data.remote.request.MovieService;
import io.romo.popularmovies.data.remote.request.ServiceGenerator;
import io.romo.popularmovies.data.remote.response.MovieReviewResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsFragment extends Fragment implements Callback<MovieReviewResponse> {
    
    private static final String ARG_MOVIE_ID = "movie_id";
    
    @BindView(R.id.reviews) RecyclerView reviews;
    private MovieReviewAdapter adapter;
    
    private int movieId;
    
    public static ReviewsFragment newInstance(int movieId) {
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        
        ReviewsFragment fragment = new ReviewsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieId = getArguments().getInt(ARG_MOVIE_ID);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reviews, container, false);
        ButterKnife.bind(this, v);
        
        reviews.setHasFixedSize(true);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        reviews.setLayoutManager(layoutManager);
        
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        reviews.addItemDecoration(divider);
        
        adapter = new MovieReviewAdapter(itemListener);
        reviews.setAdapter(adapter);
    
        MovieService service = ServiceGenerator.createService(MovieService.class);
    
        Call<MovieReviewResponse> call = service.getMovieReviews(movieId);
        call.enqueue(this);
        
        return v;
    }
    
    @Override
    public void onResponse(Call<MovieReviewResponse> call, Response<MovieReviewResponse> response) {
        // TODO: Use the movie review list and display it
        adapter.replaceData(response.body().getResults());
    }
    
    @Override
    public void onFailure(Call<MovieReviewResponse> call, Throwable t) {
        // TODO: Handle error
    }
    
    private ReviewItemListener itemListener = new ReviewItemListener() {
        @Override
        public void onReviewClick(MovieReview clickedReview) {
            // TODO Launch web browser
        }
    };
    
    private static class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewViewHolder> {
        
        private List<MovieReview> reviewList;
        private ReviewItemListener itemListener;
        
        public MovieReviewAdapter(ReviewItemListener itemListener) {
            this.itemListener = itemListener;
        }
        
        public void replaceData(List<MovieReview> reviewList) {
            this.reviewList = reviewList;
            notifyDataSetChanged();
        }
        
        @Override
        public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_movie_review, parent, false);
            return new MovieReviewViewHolder(v);
        }
        
        @Override
        public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
            MovieReview review = reviewList.get(position);
            holder.bind(review, itemListener);
        }
        
        @Override
        public int getItemCount() {
            return reviewList == null ? 0 : reviewList.size();
        }
    }
    
    static class MovieReviewViewHolder extends RecyclerView.ViewHolder {
        
        @BindView(R.id.author) TextView author;
        @BindView(R.id.content) TextView content;
        @BindView(R.id.read_full_review) Button readFullReview;
        
        private MovieReview review;
        private ReviewItemListener itemListener;
        
        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        
        public void bind(MovieReview review, ReviewItemListener itemListener) {
            this.review = review;
            this.itemListener = itemListener;
            
            author.setText(review.getAuthor());
            content.setText(review.getContent());
        }
        
        @OnClick(R.id.read_full_review)
        void readFullReviewOnClick() {
            itemListener.onReviewClick(review);
        }
    }
    
    private interface ReviewItemListener {
        
        void onReviewClick(MovieReview clickedReview);
    }
}
