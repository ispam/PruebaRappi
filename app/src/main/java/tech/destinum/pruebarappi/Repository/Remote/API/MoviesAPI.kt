package tech.destinum.pruebarappi.Repository.Remote.API

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tech.destinum.pruebarappi.Repository.Local.Entities.MoviesResult

interface MoviesAPI {

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

    @GET("search/movie")
    fun searchOnline(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("query") query: String,
            @Query("page") page: Int
    ): Observable<MoviesResult>

}