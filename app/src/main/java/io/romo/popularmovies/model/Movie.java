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

package io.romo.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String releaseDate;
    private double voteAverage;
    private int voteCount;
    private String overview;
    @Nullable private String posterPath;
    @Nullable private String backdropPath;

    public Movie() {

    }

    public Movie(int id, String title, String releaseDate, double voteAverage, int voteCount,
                 String overview, @Nullable String posterPath, @Nullable String backdropPath) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Nullable
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(@Nullable String posterPath) {
        this.posterPath = posterPath;
    }

    @Nullable
    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(@Nullable String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeInt(voteCount);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
    }

    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        voteCount = in.readInt();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
