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

package io.romo.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.model.Movie;
import io.romo.popularmovies.fragments.MovieOverviewFragment;
import io.romo.popularmovies.fragments.MovieReviewsFragment;
import io.romo.popularmovies.fragments.MovieVideosFragment;
import io.romo.popularmovies.util.NetworkUtils;

public class MovieDetailActivity extends AppCompatActivity {
    
    private static final int PAGE_LIMIT = 2;
    
    private static final String EXTRA_MOVIE = "io.romo.popularmovies.movie";
    
    @BindView(R.id.appbar) AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.backdrop) ImageView backdrop;
    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.vote_average) RatingBar voteAverage;
    @BindView(R.id.vote_count) TextView voteCount;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    
    private Movie movie;
    
    public static Intent newIntent(Context packageContext, Movie movie) {
        Intent intent = new Intent(packageContext, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }
    
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        
        setSupportActionBar(toolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        collapsingToolbar.setTitle(movie.getTitle());
        
        Picasso.with(this).load(NetworkUtils.createImageUrl(movie.getBackdropPath(), NetworkUtils.ImageSize.LARGE))
                .placeholder(R.drawable.backdrop_place_holder_w780)
                .into(backdrop);
    
        Picasso.with(this).load(NetworkUtils.createImageUrl(movie.getPosterPath(), NetworkUtils.ImageSize.SMALL))
                .placeholder(R.drawable.poster_place_holder_w300)
                .into(poster);
        
        title.setText(movie.getTitle());
        SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = currentFormat.parse(movie.getReleaseDate());
            SimpleDateFormat newFormat = new SimpleDateFormat("MMMM d, yyyy");
            releaseDate.setText(newFormat.format(date));
        } catch (ParseException e) {
            releaseDate.setText(movie.getReleaseDate());
        }
        voteAverage.setRating((float) (movie.getVoteAverage() / 2));
        voteCount.setText(NumberFormat.getNumberInstance(Locale.US).format(movie.getVoteCount()) + " Ratings");
        
        setupViewPager(viewPager);
        
        viewPager.setOffscreenPageLimit(PAGE_LIMIT);
        
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_movie_details, menu);
        return true;
    }
    
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MovieOverviewFragment.newInstance(movie.getOverview()),
                            getString(R.string.overview));
        adapter.addFragment(MovieVideosFragment.newInstance(movie.getId()),
                            getString(R.string.videos));
        adapter.addFragment(MovieReviewsFragment.newInstance(movie.getId()),
                            getString(R.string.reviews));
        viewPager.setAdapter(adapter);
    }
    
    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitles = new ArrayList<>();
        
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        
        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }
        
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        
        @Override
        public int getCount() {
            return fragments.size();
        }
        
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}
