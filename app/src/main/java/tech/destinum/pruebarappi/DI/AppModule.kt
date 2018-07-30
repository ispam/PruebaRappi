package tech.destinum.pruebarappi.DI

import android.content.Context
import dagger.Module
import dagger.Provides
import tech.destinum.pruebarappi.Activities.App
import javax.inject.Singleton

@Module
class AppModule(private val application: App) {

    @Singleton @Provides
    fun getApp(): Context = application
}