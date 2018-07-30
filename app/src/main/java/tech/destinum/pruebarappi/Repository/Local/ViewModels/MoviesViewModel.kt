package tech.destinum.pruebarappi.Repository.Local.ViewModels

import io.reactivex.Completable
import tech.destinum.pruebarappi.Repository.Local.Entities.Movie
import tech.destinum.pruebarappi.Repository.Local.PruebaRappiDB
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private val pruebaRappiDB: PruebaRappiDB) {

    fun create(movie: Movie): Completable =
            Completable.fromAction { pruebaRappiDB.movbiesDAO().insertMovie(movie) }


}