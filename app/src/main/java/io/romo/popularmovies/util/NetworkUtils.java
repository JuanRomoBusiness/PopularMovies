package io.romo.popularmovies.util;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import io.romo.popularmovies.BuildConfig;
import io.romo.popularmovies.model.SortBy;

public class NetworkUtils {

    private final static String BASE_URL = "https://api.themoviedb.org/3/movie";

    public final static String PATH_POPULAR = "popular";
    public final static String PATH_TOP_RATED = "top_rated";

    private final static String PARAM_API_KEY = "api_key";
    private final static String API_KEY = BuildConfig.API_KEY;

    public static URL buildUrl(SortBy sortBy) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sortBy == SortBy.MOST_POPULAR ? PATH_POPULAR : PATH_TOP_RATED)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
