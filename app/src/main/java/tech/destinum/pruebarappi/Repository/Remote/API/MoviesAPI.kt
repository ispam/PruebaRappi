package tech.destinum.pruebarappi.Repository.Remote.API

import retrofit2.Call
import retrofit2.http.GET
import tech.destinum.pruebarappi.Repository.Local.Entities.Result

interface MoviesAPI {

    @GET("popular?api_key=f38421ff3159b2d07010419fc5b52d04")
    fun getMovies(): Call<Result>
}