package tech.destinum.pruebarappi.Repository.Local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import tech.destinum.pruebarappi.Repository.Local.DAOs.MoviesDAO
import tech.destinum.pruebarappi.Repository.Local.Entities.Movie

@Database(entities = arrayOf(Movie::class), version = 1, exportSchema = false)
abstract class PruebaRappiDB : RoomDatabase(){

    abstract fun moviesDAO(): MoviesDAO

}