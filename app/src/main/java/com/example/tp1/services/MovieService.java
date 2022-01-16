package com.example.tp1.services;

import com.example.tp1.model.MovieDetail;
import com.example.tp1.model.PopularMovie;
import com.example.tp1.model.UpcomingMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    String ENDPOINT = "https://api.themoviedb.org/";

    @GET("3/movie/popular")
    Call<PopularMovie> getListPopularMovie(@Query("api_key") String api, @Query("page") int pageIndex);

    @GET("3/movie/upcoming")
    Call<UpcomingMovie> getListUpcomingMovie(@Query("api_key") String api, @Query("page") int pageIndex);

    @GET("3/movie/{movie_id}")
    Call<MovieDetail> getMovie(@Path("movie_id") int id , @Query("api_key") String api);

    @GET("3/search/movie")
    Call<PopularMovie> getMoviesByQuery(@Query("api_key") String apiKey,@Query("query") String query);
}
