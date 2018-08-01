package tech.destinum.pruebarappi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.pruebarappi.Activities.DetailsActivity;
import tech.destinum.pruebarappi.R;
import tech.destinum.pruebarappi.Repository.Local.ViewModels.MoviesViewModel;

public class MoviesCursorAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private MoviesViewModel moviesVM;
    private CompositeDisposable mDisposable;

    public MoviesCursorAdapter(Context context, Cursor cursor, MoviesViewModel moviesVM, CompositeDisposable mDisposable) {
        super(context, cursor, false);
        this.moviesVM = moviesVM;
        this.mDisposable = mDisposable;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.format_search, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));

        TextView titleTV = view.findViewById(R.id.search_title);
        titleTV.setText(title);

       view.setOnClickListener(v ->
               mDisposable.add(moviesVM.getMovie(title)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .doOnSuccess(movie -> {
                       Intent intent = new Intent(context, DetailsActivity.class);
                       intent.putExtra("movie", movie);
                       context.startActivity(intent);
                   })
                   .doAfterSuccess(movie -> mDisposable.clear())
                   .subscribe())
       );
    }

}