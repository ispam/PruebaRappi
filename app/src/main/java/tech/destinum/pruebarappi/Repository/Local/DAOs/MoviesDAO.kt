package tech.destinum.pruebarappi.Repository.Local.DAOs

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.database.Cursor
import io.reactivex.Flowable
import io.reactivex.Single
import tech.destinum.pruebarappi.Repository.Local.Entities.Movie

@Dao
interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMovies(movies: List<Movie>)

    @Query("select * from movies where category = :category order by page asc")
    fun getMovies(category: String): Single<List<Movie>>

    @Query("select * from movies where title LIKE :title")
    fun getDealsCursor(title: String): Cursor

    @Query("select * from movies where title = :title")
    fun getMovie(title: String): Single<Movie>

}