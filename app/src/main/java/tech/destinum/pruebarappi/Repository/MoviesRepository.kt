package tech.destinum.pruebarappi.Repository

import android.util.Log
import io.reactivex.Observable
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
        const val POPULAR = "popular"
        const val TOP_RATED = "top_rated"
        const val UPCOMING = "upcoming"
    }

    fun getPopularMovies(page: Int, callback: GetMoviesCallback, category: String): Observable<Any> {
        return Observable.concatArrayEager(
                getMoviesFromDB(category),
                getMoviesFromAPI(page, callback, category))
    }

    fun searchOnline(query: String, callback: GetMoviesCallback): Observable<MoviesResult>{
        return moviesAPI.searchOnline(API_KEY, LANG, query, 1)
                .doOnNext {
                    addMovies(it, callback, 1, "search")
                }
    }

    private fun getMoviesFromAPI(page: Int, callback: GetMoviesCallback, category: String): Observable<MoviesResult> =

            when (category) {
                POPULAR -> {
                    moviesAPI.getPopularMovies(API_KEY, LANG, page)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError { e -> Log.e("ERROR", e.message )}
                            .doOnNext { addMovies(it, callback, page, POPULAR) }
                            .subscribeOn(Schedulers.io())
                }
                TOP_RATED -> {
                    moviesAPI.getTopRatedMovies(API_KEY, LANG, page)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError { e -> Log.e("ERROR", e.message )}
                            .doOnNext { addMovies(it, callback, page, TOP_RATED) }
                            .subscribeOn(Schedulers.io())
                }
                UPCOMING -> {
                    moviesAPI.getUpcomingMovies(API_KEY, LANG, page)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError { e -> Log.e("ERROR", e.message )}
                            .doOnNext { addMovies(it, callback, page, UPCOMING) }
                            .subscribeOn(Schedulers.io())
                }
                else -> {
                    Observable.empty()
                }
            }


    private fun addMovies(result: MoviesResult, callback: GetMoviesCallback, page: Int, category: String){
        if (result.movies != null) {
            val mutableList: MutableList<Movie> = ArrayList()

            callback.onSuccess(result.page!!, result.movies as MutableList<Movie>)

            for (movie in result.movies as MutableList<Movie> ){
                mutableList.add(Movie(movie.id, movie.voteAverage, movie.title, movie.posterPath, movie.overview,
                        movie.releaseDate, page, category))
            }

            storeMoviesInDB(mutableList)
        } else {
            callback.onError()
        }
    }

    private fun getMoviesFromDB(category: String): Observable<List<Movie>> =
            moviesVM.getMovies(category).filter { it.isNotEmpty() }
                    .toObservable()
                    .doOnNext { Log.i("getMoviesFromDB", "${it.size}") }


    private fun storeMoviesInDB(movies: List<Movie>) {
                moviesVM.createAll(movies)
                        .subscribeOn(Schedulers.io())
                        .doOnComplete { Log.i("storeMoviesInDB", "${movies.size}") }
                        .subscribe()
            }
}