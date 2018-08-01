package tech.destinum.pruebarappi.Activities

import android.content.Context
import android.net.ConnectivityManager
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
import android.view.Menu
import android.view.View
import android.widget.*
import io.reactivex.android.schedulers.AndroidSchedulers
import tech.destinum.pruebarappi.Repository.Remote.API.GetMoviesCallback
import android.app.SearchManager
import tech.destinum.pruebarappi.Adapters.MoviesCursorAdapter


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
        initializeRecycler(currentPage, CATEGORY.POPULAR.value)

    }

    override fun onStop() {
        super.onStop()
        if (!mDisposable.isDisposed) mDisposable.clear()
    }

    enum class CATEGORY(val value: String) {
        POPULAR("popular"),
        TOP_RATED("top_rated"),
        UPCOMING("upcoming")
    }

    private fun initializeRecycler(page: Int, category: String){

        if (isOnline(this@MainActivity)) {

            mDisposable.add(repository.getPopularMovies(page, object : GetMoviesCallback{
                        override fun onSuccess(page: Int, movies: MutableList<Movie>) {
                               if (adapter == null) {
                                   adapter = MoviesAdapter(movies, this@MainActivity)
                                   recyclerView.adapter = adapter
                               } else{
                                   if (page == 1){
                                       adapter?.clearMovies()
                                   }
                                   adapter?.appendMovies(movies)
                               }
                               currentPage = page
                               isFetching = false
                        }
                        override fun onError() {
                            Toast.makeText(this@MainActivity, "Please check your internet connection.", Toast.LENGTH_SHORT).show()

                        }
                    }, category)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe())

            } else {

            mDisposable.add(moviesVM.getMovies(category)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess {
                        adapter = MoviesAdapter(it as MutableList<Movie>, this@MainActivity)
                        recyclerView.adapter = adapter
                        println(it.size)
                    }
                    .subscribe())
        }
    }
    private fun setupOnScrollListener(category: String) {

        val glm =  GridLayoutManager(this, 2)
        recyclerView.layoutManager = glm

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val totalItemCount = glm.itemCount
                val visibleItemCount = glm.childCount
                val firstVisibleItem = glm.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetching) {
                        getMovies((currentPage + 1), category)
                    }
                }
            }
        })
    }

    private fun getMovies(page: Int, category: String) {
        isFetching = true
        initializeRecycler(page, category)
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newQuery = "%$newText%"

                if (!isOnline(this@MainActivity)) {
                    getCursor(newQuery, searchView)
                } else {

                    getCursor(newQuery, searchView)

                    mDisposable.add(repository.searchOnline(newQuery, object : GetMoviesCallback{
                                override fun onSuccess(page: Int, movies: MutableList<Movie>) {

                                }

                                override fun onError() {
                                    Toast.makeText(this@MainActivity, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe())
                }
                return true
            }

        })

        val item = menu.findItem(R.id.spinner)
        val spinner = item.actionView as Spinner

        val adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, R.layout.spinner_dropdown_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (position){
                    0 -> {
                        initializeRecycler(1, CATEGORY.POPULAR.value)
                        setupOnScrollListener(CATEGORY.POPULAR.value)
                    }
                    1 -> {
                        initializeRecycler(1, CATEGORY.TOP_RATED.value)
                        setupOnScrollListener(CATEGORY.TOP_RATED.value)
                    }
                    2 -> {
                        initializeRecycler(1, CATEGORY.UPCOMING.value)
                        setupOnScrollListener(CATEGORY.UPCOMING.value)
                    }
                }
            }
        }
        return true
    }

    fun getCursor(newQuery: String, searchView: SearchView){
        mDisposable.add(moviesVM.getTitleCursor(newQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    searchView.suggestionsAdapter = MoviesCursorAdapter(this@MainActivity, it, moviesVM, mDisposable)
                }.subscribe())
    }


}
