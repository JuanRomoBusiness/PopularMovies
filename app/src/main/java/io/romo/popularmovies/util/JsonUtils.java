package io.romo.popularmovies.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.romo.popularmovies.model.Movie;

public class JsonUtils {

    public static List<Movie> getMovies(String response) throws JSONException {

        final String RESULTS = "results";

        final String ID = "id";
        final String TITLE = "title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";

        final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w300";

        JSONObject responseJson = new JSONObject(response);

        JSONArray moviesJson = responseJson.getJSONArray(RESULTS);

        List<Movie> movieList = new ArrayList<>();

        for (int i = 0; i < moviesJson.length(); i++) {
            JSONObject movieJson = moviesJson.getJSONObject(i);

            int id = movieJson.getInt(ID);
            String title = movieJson.getString(TITLE);
            String posterPath = BASE_IMAGE_URL + movieJson.getString(POSTER_PATH);
            String overview = movieJson.getString(OVERVIEW);
            double voteAverage = movieJson.getDouble(VOTE_AVERAGE);
            String releaseDate = movieJson.getString(RELEASE_DATE);

            Movie movie = new Movie(id, title, posterPath, overview, voteAverage, releaseDate);
            movieList.add(movie);
        }

        return movieList;
    }
}