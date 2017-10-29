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

package io.romo.popularmovies.data.remote.request;

import io.romo.popularmovies.data.remote.response.MovieResponse;
import io.romo.popularmovies.data.remote.response.MovieReviewResponse;
import io.romo.popularmovies.data.remote.response.MovieVideoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieService {
    
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies();
    
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies();
    
    @GET("movie/{movie_id}/videos")
    Call<MovieVideoResponse> getMovieVideos(@Path("movie_id") int movieId);
    
    @GET("movie/{movie_id}/reviews")
    Call<MovieReviewResponse> getMovieReviews(@Path("movie_id") int movieId);
}
