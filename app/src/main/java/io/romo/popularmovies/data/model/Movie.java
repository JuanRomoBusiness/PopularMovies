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

package io.romo.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {
    
    private int id;
    private String title;
    private String releaseDate;
    private double voteAverage;
    private int voteCount;
    private double popularity;
    private String overview;
    private boolean video;
    @Nullable private String posterPath;
    @Nullable private String backdropPath;
    private String originalTitle;
    private String originalLanguage;
    private List<Integer> genreIds;
    private boolean adult;
    
    public Movie() {
        
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
    
    public double getPopularity() {
        return popularity;
    }
    
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }
    
    public String getOverview() {
        return overview;
    }
    
    public void setOverview(String overview) {
        this.overview = overview;
    }
    
    public boolean isVideo() {
        return video;
    }
    
    public void setVideo(boolean video) {
        this.video = video;
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
    
    public String getOriginalTitle() {
        return originalTitle;
    }
    
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    
    public String getOriginalLanguage() {
        return originalLanguage;
    }
    
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }
    
    public List<Integer> getGenreIds() {
        return genreIds;
    }
    
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }
    
    public boolean isAdult() {
        return adult;
    }
    
    public void setAdult(boolean adult) {
        this.adult = adult;
    }
    
    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
        voteCount = in.readInt();
        popularity = in.readDouble();
        overview = in.readString();
        video = in.readByte() != 0;
        posterPath = in.readString();
        backdropPath = in.readString();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        genreIds = new ArrayList<>();
        in.readList(genreIds, Integer.class.getClassLoader());
        adult = in.readByte() != 0;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeInt(voteCount);
        dest.writeDouble(popularity);
        dest.writeString(overview);
        dest.writeByte(video ? (byte) 1 : (byte) 0);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeList(genreIds);
        dest.writeByte(adult ? (byte) 1 : (byte) 0);
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
