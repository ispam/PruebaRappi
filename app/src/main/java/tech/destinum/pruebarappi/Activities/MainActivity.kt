package tech.destinum.pruebarappi.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.destinum.pruebarappi.R
import tech.destinum.pruebarappi.Repository.MoviesRepository
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private val mDisposable: CompositeDisposable = CompositeDisposable()

    @Inject
    lateinit var repository: MoviesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.inject(this)

        mDisposable.add(repository.getMovies()
                .subscribeOn(Schedulers.io())
                .subscribe())

    }
}
