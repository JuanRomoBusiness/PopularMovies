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

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.model.MovieVideo;
import io.romo.popularmovies.rest.service.TheMovieDbService;
import io.romo.popularmovies.rest.TheMovieDbClient;
import io.romo.popularmovies.rest.response.MovieVideosResponse;
import io.romo.popularmovies.util.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieVideosFragment extends Fragment implements Callback<MovieVideosResponse> {
    
    private static final String ARG_MOVIE_ID = "movie_id";

    private static final String SAVED_LIST_POSITION = "list_position";
    
    @BindView(R.id.movie_videos) RecyclerView movieVideos;
    private MovieVideosAdapter adapter;
    
    private int movieId;
    private Parcelable savedListPosition;
    
    public static MovieVideosFragment newInstance(int movieId) {
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        
        MovieVideosFragment fragment = new MovieVideosFragment();
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
        View v = inflater.inflate(R.layout.fragment_movie_videos, container, false);
        ButterKnife.bind(this, v);
        
        movieVideos.setHasFixedSize(true);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        movieVideos.setLayoutManager(layoutManager);
        
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        movieVideos.addItemDecoration(divider);
        
        adapter = new MovieVideosAdapter(itemListener);
        movieVideos.setAdapter(adapter);
    
        TheMovieDbClient.createService(TheMovieDbService.class)
                .getMovieVideos(movieId)
                .enqueue(this);
        
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_LIST_POSITION,
                movieVideos.getLayoutManager().onSaveInstanceState());
    }
    
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResponse(Call<MovieVideosResponse> call, Response<MovieVideosResponse> response) {
        if (response.isSuccessful()) {
            adapter.replaceData(response.body().getResults());
            movieVideos.getLayoutManager().onRestoreInstanceState(savedListPosition);
        } else {
            // TODO: Handle error
        }
    }
    
    @Override
    public void onFailure(Call<MovieVideosResponse> call, Throwable t) {
        // TODO: Handle error
    }
    
    private MovieVideoItemListener itemListener = new MovieVideoItemListener() {
        @Override
        public void onMovieVideoClick(MovieVideo clickedMovieVideo) {
            NetworkUtils.watchYoutubeVideo(getActivity(), clickedMovieVideo.getKey());
        }
    };
    
    static class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosViewHolder> {
        
        private List<MovieVideo> movieVideoList;
        private MovieVideoItemListener itemListener;
        
        public MovieVideosAdapter(MovieVideoItemListener itemListener) {
            this.itemListener = itemListener;
        }
        
        public void replaceData(List<MovieVideo> movieVideoList) {
            this.movieVideoList = movieVideoList;
            notifyDataSetChanged();
        }
        
        @Override
        public MovieVideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_movie_video, parent, false);
            return new MovieVideosViewHolder(v);
        }
        
        @Override
        public void onBindViewHolder(MovieVideosViewHolder holder, int position) {
            MovieVideo movieVideo = movieVideoList.get(position);
            holder.bind(movieVideo, itemListener);
        }
        
        @Override
        public int getItemCount() {
            return movieVideoList == null ? 0 : movieVideoList.size();
        }
    }
    
    static class MovieVideosViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        
        @BindView(R.id.name) TextView name;
        
        private MovieVideo movieVideo;
        private MovieVideoItemListener itemListener;
        
        public MovieVideosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            
            itemView.setOnClickListener(this);
        }
        
        public void bind(MovieVideo video, MovieVideoItemListener itemListener) {
            this.movieVideo = video;
            this.itemListener = itemListener;
            
            name.setText(video.getName());
        }
        
        @Override
        public void onClick(View view) {
            itemListener.onMovieVideoClick(movieVideo);
        }
    }
    
    private interface MovieVideoItemListener {
        
        void onMovieVideoClick(MovieVideo clickedMovieVideo);
    }
}
