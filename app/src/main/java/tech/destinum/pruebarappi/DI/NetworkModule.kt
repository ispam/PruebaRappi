package tech.destinum.pruebarappi.DI

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tech.destinum.pruebarappi.BuildConfig
import tech.destinum.pruebarappi.Repository.Remote.API.MoviesAPI
import java.io.File
import javax.inject.Singleton
import okhttp3.*
import android.net.ConnectivityManager

@Module(includes = arrayOf(AppModule::class))
class NetworkModule {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    @Singleton @Provides
    fun provideAPI(retrofit: Retrofit): MoviesAPI  = retrofit.create(MoviesAPI::class.java)

    @Singleton @Provides
    fun provideCache(cacheFile: File): Cache = Cache(cacheFile, 10 * 1024 * 1024)

    @Singleton @Provides
    fun provideFile(context: Context): File = File(context.cacheDir, "okhttp_cache")

    @Singleton @Provides
    fun provideLogginInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            message -> Log.v("LogginInterceptor", message)
        })
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return  interceptor
    }

    @Singleton @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder().client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

    @Singleton @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor, cache: Cache, context: Context): OkHttpClient{

        val ONLINE_INTERCEPTOR = Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val maxAge = 0 // read from cache
            val cacheControl: String = response.header("Cache-Control")!!
            if (cacheControl.contains("no-store") || cacheControl.contains("no-cache")
                    || cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")){
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=$maxAge")
                        .build()
            } else {
                response
            }

        }
        val OFFLINE_INTERCEPTOR = Interceptor { chain ->
            var request = chain.request()
            if (!isOnline(context)) {
                val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                request = request.newBuilder()
                        .removeHeader("Pragma")
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .build()
            }
            chain.proceed(request)
        }

        return if (BuildConfig.DEBUG){
            OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(httpLoggingInterceptor)
                    .addNetworkInterceptor(ONLINE_INTERCEPTOR)
                    .addInterceptor(OFFLINE_INTERCEPTOR)
                    .build()
        } else {
            OkHttpClient.Builder()
                    .cache(cache)
                    .addNetworkInterceptor(ONLINE_INTERCEPTOR)
                    .addInterceptor(OFFLINE_INTERCEPTOR)
                    .build()
        }
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

}