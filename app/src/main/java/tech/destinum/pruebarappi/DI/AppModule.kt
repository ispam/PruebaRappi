package tech.destinum.pruebarappi.DI

import android.content.Context
import dagger.Module
import dagger.Provides
import tech.destinum.pruebarappi.Activities.App
import tech.destinum.pruebarappi.Repository.Local.ViewModels.MoviesViewModel
import tech.destinum.pruebarappi.Repository.MoviesRepository
import tech.destinum.pruebarappi.Repository.Remote.API.MoviesAPI
import javax.inject.Singleton

@Module(includes = arrayOf(NetworkModule::class, ViewModelModule::class))
class AppModule(private val application: App) {

    @Singleton @Provides
    fun getApp(): Context = application

    @Singleton @Provides
    fun provideRepository(moviesAPI: MoviesAPI, moviesVM: MoviesViewModel): MoviesRepository =
            MoviesRepository(moviesAPI, moviesVM)

}