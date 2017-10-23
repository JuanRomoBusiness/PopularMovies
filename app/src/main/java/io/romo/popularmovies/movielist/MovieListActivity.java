package io.romo.popularmovies.movielist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.util.ActivityUtils;

public class MovieListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        MovieListFragment movieListFragment =
                (MovieListFragment) getSupportFragmentManager().findFragmentById(R.id.movie_list_container);
        if (movieListFragment == null) {
            movieListFragment = new MovieListFragment();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), movieListFragment, R.id.movie_list_container);
        }
    }
}
