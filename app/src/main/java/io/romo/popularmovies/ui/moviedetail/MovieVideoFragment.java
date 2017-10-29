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
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.data.model.MovieVideo;
import io.romo.popularmovies.data.remote.request.MovieService;
import io.romo.popularmovies.data.remote.request.ServiceGenerator;
import io.romo.popularmovies.data.remote.response.MovieVideoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieVideoFragment extends Fragment implements Callback<MovieVideoResponse> {
    
    private static final String ARG_MOVIE_ID = "movie_id";
    
    @BindView(R.id.videos) RecyclerView videos;
    private MovieVideoAdapter adapter;
    
    private int movieId;
    
    public static MovieVideoFragment newInstance(int movieId) {
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        
        MovieVideoFragment fragment = new MovieVideoFragment();
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
        View v = inflater.inflate(R.layout.fragment_videos, container, false);
        ButterKnife.bind(this, v);
        
        videos.setHasFixedSize(true);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        videos.setLayoutManager(layoutManager);
        
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        videos.addItemDecoration(divider);
        
        adapter = new MovieVideoAdapter(itemListener);
        videos.setAdapter(adapter);
    
        MovieService service = ServiceGenerator.createService(MovieService.class);
    
        Call<MovieVideoResponse> call = service.getMovieVideos(movieId);
        call.enqueue(this);
        
        return v;
    }
    
    @Override
    public void onResponse(Call<MovieVideoResponse> call, Response<MovieVideoResponse> response) {
        // TODO: Use the movie video list and display it
        adapter.replaceData(response.body().getResults());
    }
    
    @Override
    public void onFailure(Call<MovieVideoResponse> call, Throwable t) {
        // TODO: Handle error
    }
    
    private VideoItemListener itemListener = new VideoItemListener() {
        @Override
        public void onVideoClick(MovieVideo clickedVideo) {
            // TODO: Play Youtube video
        }
    };
    
    static class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoViewHolder> {
        
        private List<MovieVideo> videoList;
        private VideoItemListener itemListener;
        
        public MovieVideoAdapter(VideoItemListener itemListener) {
            this.itemListener = itemListener;
        }
        
        public void replaceData(List<MovieVideo> videoList) {
            this.videoList = videoList;
            notifyDataSetChanged();
        }
        
        @Override
        public MovieVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_movie_video, parent, false);
            return new MovieVideoViewHolder(v);
        }
        
        @Override
        public void onBindViewHolder(MovieVideoViewHolder holder, int position) {
            MovieVideo video = videoList.get(position);
            holder.bind(video, itemListener);
        }
        
        @Override
        public int getItemCount() {
            return videoList == null ? 0 : videoList.size();
        }
    }
    
    static class MovieVideoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        
        @BindView(R.id.name) TextView name;
        
        private MovieVideo video;
        private VideoItemListener itemListener;
        
        public MovieVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            
            itemView.setOnClickListener(this);
        }
        
        public void bind(MovieVideo video, VideoItemListener itemListener) {
            this.video = video;
            this.itemListener = itemListener;
            
            name.setText(video.getName());
        }
        
        @Override
        public void onClick(View view) {
            itemListener.onVideoClick(video);
        }
    }
    
    private interface VideoItemListener {
        
        void onVideoClick(MovieVideo clickedVideo);
    }
}
