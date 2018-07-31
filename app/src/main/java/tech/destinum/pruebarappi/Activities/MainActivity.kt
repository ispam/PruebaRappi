package tech.destinum.pruebarappi.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.destinum.pruebarappi.Adapters.MoviesAdapter
import tech.destinum.pruebarappi.R
import tech.destinum.pruebarappi.Repository.Local.Entities.Movie
import tech.destinum.pruebarappi.Repository.Local.ViewModels.MoviesViewModel
import tech.destinum.pruebarappi.Repository.MoviesRepository
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val mDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var moviesVM: MoviesViewModel

    @Inject
    lateinit var repository: MoviesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.inject(this)

        recyclerView = findViewById(R.id.movies_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        mDisposable.add(moviesVM.getMovies()
                .subscribeOn(Schedulers.io())
                .doOnSuccess {
                    recyclerView.adapter = MoviesAdapter(it, this)
                }
                .subscribe())

//        mDisposable.add(repository.getMovies()
//                .subscribeOn(Schedulers.io())
//                .doOnNext {
//                    //                    recyclerView.adapter = MoviesAdapter(it)
//                }
//                .subscribe())

    }
}
