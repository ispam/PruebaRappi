package tech.destinum.pruebarappi.Repository.Local.DAOs

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import tech.destinum.pruebarappi.Repository.Local.Entities.Movie

@Dao
interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)
}