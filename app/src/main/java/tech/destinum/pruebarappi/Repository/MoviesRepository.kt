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
import tech.destinum.pruebarappi.Repository.Local.Entities.Result

class MoviesRepository @Inject constructor(private val moviesAPI: MoviesAPI, private val moviesVM: MoviesViewModel) {

    fun getMovies(): Observable<Any> {
        return Observable.concatArray(
                getMoviesFromDB(),
                getMoviesFromAPI())
    }

    private fun getMoviesFromAPI(): Observable<Call<Result>> =
            Observable.just(moviesAPI.getMovies())
                    .doOnNext {

                        val call: Call<Result> = it
                        call.enqueue(object : Callback<Result> {
                            override fun onFailure(call: Call<Result>?, t: Throwable?) {}

                            override fun onResponse(call: Call<Result>, response: Response<Result>) {

                                val movies = response.body()!!.mMovies

                                val mutableList: MutableList<Movie> = ArrayList()

                                for (movie in movies as List<Movie>){
                                    val voteCount: Int? = movie.voteCount
                                    val id: Int? = movie.id
                                    val video: Boolean? = movie.video
                                    val voteAverage: Double? = movie.voteAverage
                                    val title: String? = movie.title
                                    val popularity: Double? = movie.popularity
                                    val posterPath: String? = movie.posterPath
                                    val originalLanguage: String? = movie.originalLanguage
                                    val originalTitle: String? = movie.originalTitle
                                    val backdropPath: String? = movie.backdropPath
                                    val adult: Boolean? = movie.adult
                                    val overview: String? = movie.overview
                                    val releaseDate: String? = movie.releaseDate

                                    val movieEntity = Movie(voteCount,id, video, voteAverage, title, popularity, posterPath, originalLanguage, originalTitle, backdropPath, adult, overview, releaseDate)
                                    mutableList.add(movie)
                                    println(movieEntity.title)
                                }

                                storeMoviesInDB(mutableList)
                            }
                        })
                    }

    private fun storeMoviesInDB(movies: List<Movie>) {
            moviesVM.createAll(movies).subscribeOn(Schedulers.io())
                    .doOnComplete { Log.i("storeMoviesInDB", "${movies.size}") }
                    .subscribe()
    }

    private fun getMoviesFromDB(): Observable<List<Movie>> =
            moviesVM.getMovies().filter { it.isNotEmpty() }
                    .toObservable()
                    .doOnNext { Log.i("getMoviesFromDB", "${it.size}") }
}