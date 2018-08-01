package tech.destinum.pruebarappi.Repository.Remote.API

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tech.destinum.pruebarappi.Repository.Local.Entities.MoviesResult

interface MoviesAPI {

    @GET("top_rated?api_key=f38421ff3159b2d07010419fc5b52d04&language=en-US&page={page}")
    fun getTopRatedMovies(@Path("page")page: Int): Call<MoviesResult>

    @GET("upcoming?api_key=f38421ff3159b2d07010419fc5b52d04&language=en-US&page={page}")
    fun getUpcomingMovies(@Path("page")page: Int): Call<MoviesResult>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("page") page: Int
    ): Observable<MoviesResult>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("page") page: Int
    ): Observable<MoviesResult>

    @GET("movie/popular")
    fun getPopularMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("page") page: Int
    ): Observable<MoviesResult>


}