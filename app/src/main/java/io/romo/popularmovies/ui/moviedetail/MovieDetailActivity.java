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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.data.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {
    
    private static final int PAGE_LIMIT = 2;
    
    private static final String EXTRA_MOVIE = "io.romo.popularmovies.movie";
    
    @BindView(R.id.toolbar) Toolbar toolbar;
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
        
        toolbar.setTitle(movie.getTitle());
        
        setSupportActionBar(toolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        setupViewPager(viewPager);
        
        viewPager.setOffscreenPageLimit(PAGE_LIMIT);
        
        tabLayout.setupWithViewPager(viewPager);
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
