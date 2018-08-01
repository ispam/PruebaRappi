package tech.destinum.pruebarappi.DI

import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tech.destinum.pruebarappi.BuildConfig
import tech.destinum.pruebarappi.Repository.Remote.API.MoviesAPI
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    @Singleton @Provides
    fun provideAPI(retrofit: Retrofit): MoviesAPI  = retrofit.create(MoviesAPI::class.java)

    @Singleton @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient{
        return if (BuildConfig.DEBUG){
            OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build()
        } else {
            OkHttpClient.Builder()
                    .build()
        }
    }


    @Singleton @Provides
    fun provideLogginInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            message -> Log.v("LogginInterceptor", message)
        })
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return  interceptor
    }

    @Singleton @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder().client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
}