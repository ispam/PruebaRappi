package tech.destinum.pruebarappi.DI

import dagger.Component
import tech.destinum.pruebarappi.Activities.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class, ViewModelModule::class))
interface AppComponent {

    fun inject(activity: MainActivity)
}