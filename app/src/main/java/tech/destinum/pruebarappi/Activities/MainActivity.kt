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
import android.widget.ArrayAdapter
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import tech.destinum.pruebarappi.Repository.Remote.API.GetMoviesCallback
import android.support.v7.widget.LinearLayoutManager








class MainActivity : AppCompatActivity() {

    private val mDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var recyclerView: RecyclerView
    private var isFetching: Boolean = false
    private var currentPage = 1
    private var adapter: MoviesAdapter? = null

    @Inject
    lateinit var moviesVM: MoviesViewModel

    @Inject
    lateinit var repository: MoviesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.inject(this)

        recyclerView = findViewById(R.id.movies_recycler_view)


//        mDisposable.add(moviesVM.getFlowableMovies()
//                .subscribeOn(Schedulers.io())
//                .doOnNext {
//                    recyclerView.adapter = MoviesAdapter(it, this)
//                }
//                .subscribe())

        initializeRecycler(currentPage)

        setupOnScrollListener()

    }

    private fun initializeRecycler(page: Int){
        mDisposable.add(repository.getMovies(page, object : GetMoviesCallback{
                    override fun onSuccess(page: Int, movies: MutableList<Movie>) {

                        if (adapter == null) {
                            adapter = MoviesAdapter(movies, this@MainActivity)
                            recyclerView.adapter = adapter
                        } else{
                            adapter?.appendMovies(movies)
                        }

                        currentPage = page
                        isFetching = false

                    }
                    override fun onError() {
                        Toast.makeText(this@MainActivity, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe())
    }
    private fun setupOnScrollListener() {

        val glm =  GridLayoutManager(this, 2)
        recyclerView.layoutManager = glm

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val totalItemCount = glm.itemCount
                val visibleItemCount = glm.childCount
                val firstVisibleItem = glm.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetching) {
                        getMovies(currentPage + 1)
                    }
                }
            }
        })
    }

    private fun getMovies(page: Int) {
        isFetching = true
       initializeRecycler(page)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)

        val item = menu.findItem(R.id.spinner)
        val spinner = item.actionView as Spinner

        val adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, R.layout.spinner_dropdown_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(view!!.context, "${position + 1}", Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }

}
