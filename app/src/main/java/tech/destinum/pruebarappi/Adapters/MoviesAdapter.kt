package tech.destinum.pruebarappi.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import tech.destinum.pruebarappi.R
import tech.destinum.pruebarappi.Repository.Local.Entities.Movie

class MoviesAdapter(private val moviesList: List<Movie>): RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.format_movie, parent, false))
    }

    override fun getItemCount(): Int {
        return moviesList?.size ?: 0
    }

    override fun onBindViewHolder(holder: MoviesAdapter.ViewHolder, position: Int) {
        val movie = moviesList[position]
        holder.bind(movie)
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){

        private var image: ImageView = v.findViewById(R.id.format_movie_image)
        private var title: TextView = v.findViewById(R.id.format_movie_title)
        private var genres: TextView = v.findViewById(R.id.format_movie_title)

        fun bind(movie: Movie){

            title.text = movie.title
            genres.text = movie.overview
        }
    }
}