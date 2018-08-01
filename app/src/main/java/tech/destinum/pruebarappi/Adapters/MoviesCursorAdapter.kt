package tech.destinum.pruebarappi.Adapters

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.destinum.pruebarappi.Activities.DetailsActivity
import tech.destinum.pruebarappi.R
import tech.destinum.pruebarappi.Repository.Local.ViewModels.MoviesViewModel

class MoviesCursorAdapter(context: Context, cursor: Cursor, private val moviesVM: MoviesViewModel, private val mDisposable: CompositeDisposable) : CursorAdapter(context, cursor, false) {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return mLayoutInflater.inflate(R.layout.format_search, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))

        val titleTV = view.findViewById<TextView>(R.id.search_title)
        titleTV.text = title

        view.setOnClickListener { v ->
            mDisposable.add(moviesVM.getMovie(title)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { movie ->
                        val intent = Intent(context, DetailsActivity::class.java)
                        intent.putExtra("movie", movie)
                        context.startActivity(intent)
                    }
                    .doAfterSuccess { mDisposable.clear() }
                    .subscribe())
        }
    }

}