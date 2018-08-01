package tech.destinum.pruebarappi.Repository.Local.ViewModels

import android.database.Cursor
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import tech.destinum.pruebarappi.Repository.Local.Entities.Movie
import tech.destinum.pruebarappi.Repository.Local.PruebaRappiDB
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private val pruebaRappiDB: PruebaRappiDB) {

    fun create(movie: Movie): Completable =
            Completable.fromAction { pruebaRappiDB.moviesDAO().insertMovie(movie) }

    fun createAll(movies: List<Movie>): Completable =
            Completable.fromAction { pruebaRappiDB.moviesDAO().insertAllMovies(movies) }

    fun getMovies(category: String): Single<List<Movie>> =
            pruebaRappiDB.moviesDAO().getMovies(category)

    fun getTitleCursor(query: String): Single<Cursor> =
            Single.fromCallable { pruebaRappiDB.moviesDAO().getDealsCursor(query) }

    fun getMovie(title: String): Single<Movie> =
            pruebaRappiDB.moviesDAO().getMovie(title)



}