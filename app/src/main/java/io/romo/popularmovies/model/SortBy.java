package io.romo.popularmovies.model;

import io.romo.popularmovies.util.NetworkUtils;

public enum SortBy {
    MOST_POPULAR(NetworkUtils.PATH_POPULAR),
    HIGHEST_RATED(NetworkUtils.PATH_TOP_RATED);

    String path;

    SortBy(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
