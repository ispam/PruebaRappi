package tech.destinum.pruebarappi.DI

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import tech.destinum.pruebarappi.Repository.Local.PruebaRappiDB
import tech.destinum.pruebarappi.Repository.Local.ViewModels.MoviesViewModel
import javax.inject.Singleton

@Module
class ViewModelModule {

    companion object {
        const val DB_NAME = "prueba_rappi.db"
    }

    @Singleton @Provides
    fun getDB(context: Context): PruebaRappiDB {
        return Room.databaseBuilder(
                context.applicationContext,
                PruebaRappiDB::class.java,
                DB_NAME)
                .build()
    }

    @Singleton @Provides
    fun getMoviesVM(pruebaRappiDB: PruebaRappiDB): MoviesViewModel = MoviesViewModel(pruebaRappiDB)

}