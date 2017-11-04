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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;

public class MovieOverviewFragment extends Fragment {
    
    private static final String ARG_MOVIE_OVERVIEW = "movie_overview";
    
    @BindView(R.id.overview) TextView overview;
    
    private String movieOverview;
    
    public static MovieOverviewFragment newInstance(String movieOverview) {
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_OVERVIEW, movieOverview);
        
        MovieOverviewFragment fragment = new MovieOverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieOverview = getArguments().getString(ARG_MOVIE_OVERVIEW);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_overview, container, false);
        ButterKnife.bind(this, v);
    
        overview.setText(movieOverview);
        
        return v;
    }
}
