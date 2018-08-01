package tech.destinum.pruebarappi.Repository.Remote.API

import tech.destinum.pruebarappi.Repository.Local.Entities.Movie

interface GetMoviesCallback {

    fun onSuccess(page: Int, movies: MutableList<Movie>)

    fun onError()
}