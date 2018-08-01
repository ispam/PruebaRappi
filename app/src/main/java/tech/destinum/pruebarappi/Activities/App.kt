package tech.destinum.pruebarappi.Activities

import android.app.Application
import android.content.Context
import android.util.Log
import tech.destinum.pruebarappi.DI.AppComponent
import tech.destinum.pruebarappi.DI.AppModule
import tech.destinum.pruebarappi.DI.DaggerAppComponent

class App: Application() {

    companion object {
        @JvmStatic lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

    }

}