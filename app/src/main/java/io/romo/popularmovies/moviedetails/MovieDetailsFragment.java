package io.romo.popularmovies.moviedetails;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.model.Movie;

public class MovieDetailsFragment extends Fragment {

    private static final String ARG_MOVIE = "movie";

    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.vote_average) RatingBar voteAverage;
    @BindView(R.id.overview) TextView overview;

    private Movie movie;

    public static MovieDetailsFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = getArguments().getParcelable(ARG_MOVIE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);

        Context context = getActivity();
        Picasso.with(context).load(movie.getPosterPath())
                .placeholder(R.drawable.place_holder_w300)
                .into(poster);
        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        // Normalizing rating from 0-10 to 0-5
        voteAverage.setRating((float) (movie.getVoteAverage() / 2));
        releaseDate.setText(movie.getReleaseDate());

        return view;
    }
}
