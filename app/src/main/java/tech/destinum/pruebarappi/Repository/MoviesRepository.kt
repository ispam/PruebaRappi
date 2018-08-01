package tech.destinum.pruebarappi.Repository

import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import tech.destinum.pruebarappi.Repository.Local.Entities.Movie
import tech.destinum.pruebarappi.Repository.Local.ViewModels.MoviesViewModel
import tech.destinum.pruebarappi.Repository.Remote.API.MoviesAPI
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.destinum.pruebarappi.Repository.Local.Entities.MoviesResult
import tech.destinum.pruebarappi.Repository.Remote.API.GetMoviesCallback

class MoviesRepository @Inject constructor(private val moviesAPI: MoviesAPI, private val moviesVM: MoviesViewModel) {


    companion object {
        const val API_KEY = "f38421ff3159b2d07010419fc5b52d04"
        const val LANG = "en-US"
    }
    fun getMovies(page: Int, callback: GetMoviesCallback): Observable<Any> {
        return Observable.concatArrayEager(
                getMoviesFromDB(),
                getMoviesFromAPI(page, callback))
    }

    private fun getMoviesFromAPI(page: Int, callback: GetMoviesCallback): Observable<Call<MoviesResult>> =
            Observable.just(moviesAPI.getPopularMovies(API_KEY, LANG, page))
                    .doOnNext {

                        val call: Call<MoviesResult> = it
                        call.enqueue(object : Callback<MoviesResult> {
                            override fun onFailure(call: Call<MoviesResult>?, t: Throwable?) {
                            }

                            override fun onResponse(call: Call<MoviesResult>, response: Response<MoviesResult>) {

                                if (response.isSuccessful){
                                    val moviesResult = response.body()
                                    if (moviesResult?.movies != null){
                                        callback.onSuccess(moviesResult.page!!, moviesResult.movies as MutableList<Movie>)

                                        val mutableList: MutableList<Movie> = ArrayList()
                                        for (movie in moviesResult.movies as MutableList<Movie>){
                                            mutableList.add(movie)
                                        }
                                        storeMoviesInDB(mutableList)
                                    } else {
                                        callback.onError()
                                    }
                                } else {
                                    getMoviesFromDB()
                                    callback.onError()
                                }
                            }
                        })
                    }



    private fun getMoviesFromDB(): Observable<List<Movie>> =
            moviesVM.getMovies().filter { it.isNotEmpty() }
                    .toObservable()
                    .doOnNext { Log.i("getMoviesFromDB", "${it.size}") }


    private fun storeMoviesInDB(movies: List<Movie>) {
                moviesVM.createAll(movies).subscribeOn(Schedulers.io())
                        .doOnComplete { Log.i("storeMoviesInDB", "${movies.size}") }
                        .subscribe()
            }
}