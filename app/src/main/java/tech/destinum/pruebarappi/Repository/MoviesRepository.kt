package tech.destinum.pruebarappi.Repository

import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import tech.destinum.pruebarappi.Repository.Local.Entities.Movie
import tech.destinum.pruebarappi.Repository.Local.ViewModels.MoviesViewModel
import tech.destinum.pruebarappi.Repository.Remote.API.MoviesAPI
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val moviesAPI: MoviesAPI, private val moviesVM: MoviesViewModel) {

    fun getMovies(): Observable<List<Any>> =
            Observable.concatArray(
                    getMoviesFromDB(),
                    getMoviesFromAPI())

    private fun getMoviesFromAPI(): Observable<List<Movie>> =
            moviesAPI.getMovies()
                    .doOnNext {
                        Log.d("Movies Repository",  "= ${it.size}")
                        storeMoviesInDB(it)
                    }

    private fun storeMoviesInDB(movies: List<Movie>) {
            moviesVM.createAll(movies).subscribeOn(Schedulers.io())
                    .doOnComplete { Log.i("Movies Repository", "${movies.size}") }
                    .subscribe()
    }

    private fun getMoviesFromDB(): Observable<List<Movie>> =
            moviesVM.getMovies().filter { it.isNotEmpty() }
                    .toObservable()
                    .doOnNext { Log.i("Movies Repository", "${it.size}") }
}