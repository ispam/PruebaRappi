package tech.destinum.pruebarappi.Repository

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tech.destinum.pruebarappi.Repository.Local.Entities.Movie
import tech.destinum.pruebarappi.Repository.Local.ViewModels.MoviesViewModel
import tech.destinum.pruebarappi.Repository.Remote.API.MoviesAPI
import javax.inject.Inject
import tech.destinum.pruebarappi.Repository.Local.Entities.MoviesResult
import tech.destinum.pruebarappi.Repository.Remote.API.GetMoviesCallback

class MoviesRepository @Inject constructor(private val moviesAPI: MoviesAPI, private val moviesVM: MoviesViewModel) {

    companion object {
        const val API_KEY = "f38421ff3159b2d07010419fc5b52d04"
        const val LANG = "en-US"
    }

    fun getPopularMovies(page: Int, callback: GetMoviesCallback): Observable<Any> {
        return Observable.concatArrayEager(
                getMoviesFromDB(),
                getMoviesFromAPI(page, callback))

    }

    private fun getMoviesFromAPI(page: Int, callback: GetMoviesCallback): Observable<MoviesResult> =
               moviesAPI.getPopularMovies(API_KEY, LANG, page)
                       .observeOn(AndroidSchedulers.mainThread())
                       .doOnError { e -> Log.e("ERROR", e.message )}
                       .doOnNext {
                           if (it?.movies != null) {
                               val mutableList: MutableList<Movie> = ArrayList()

                               callback.onSuccess(it.page!!, it.movies as MutableList<Movie>)

                               for (movie in it.movies as MutableList<Movie>){
                                   mutableList.add(Movie(movie.id, movie.voteAverage, movie.title, movie.posterPath, movie.overview,
                                           movie.releaseDate, it.page!!))
                               }

                               storeMoviesInDB(mutableList)
                           } else {
                               callback.onError()
                           }

                       }
                       .subscribeOn(Schedulers.io())


    private fun getMoviesFromDB(): Observable<List<Movie>> =
            moviesVM.getMovies().filter { it.isNotEmpty() }
                    .toObservable()
                    .doOnNext { Log.i("getMoviesFromDB", "${it.size}") }


    private fun storeMoviesInDB(movies: List<Movie>) {
                moviesVM.createAll(movies)
                        .subscribeOn(Schedulers.io())
                        .doOnComplete { Log.i("storeMoviesInDB", "${movies.size}") }
                        .subscribe()
            }
}