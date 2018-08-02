package tech.destinum.pruebarappi.Adapters

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.ldoublem.loadingviewlib.view.LVBlock
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import tech.destinum.pruebarappi.Activities.DetailsActivity
import tech.destinum.pruebarappi.R
import tech.destinum.pruebarappi.Repository.Local.Entities.Movie
import java.lang.Exception
import java.util.*


class MoviesAdapter(private val moviesList: MutableList<Movie>, private val activity: Activity): RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.format_movie, parent, false))
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    override fun onBindViewHolder(holder: MoviesAdapter.ViewHolder, position: Int) {
        val movie = moviesList[position]
        holder.bind(movie)

        holder.itemView.setOnClickListener {

            movie.category = "something"
//            Toast.makeText(holder.itemView.context, movie.category, Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra("movie", movie)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        }
    }

    fun appendMovies(moviesToAppend: List<Movie>) {
        moviesList.addAll(moviesToAppend)
        notifyDataSetChanged()
    }

    fun clearMovies() {
        moviesList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){

        private var image: ImageView = v.findViewById(R.id.format_movie_image)
        private var title: TextView = v.findViewById(R.id.format_movie_title)
        private var rating: TextView = v.findViewById(R.id.format_movie_rating)
        private var mCL: ConstraintLayout = v.findViewById(R.id.format_movie_CL)
        private var mLVBlock: LVBlock = v.findViewById(R.id.lvBlock)

        fun bind(movie: Movie){

            mLVBlock.visibility = View.VISIBLE
            mLVBlock.isShadow(false)
            mLVBlock.startAnim()

            mCL.setBackgroundColor(Color.parseColor(getColor(Random().nextInt(15))))

            rating.text = movie.voteAverage.toString()
            title.text = movie.title

//            Log.v("bind", movie.category)

            // Need onPreDraw Listener to get actual width and height.
            val vto = image.viewTreeObserver
            vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    image.viewTreeObserver.removeOnPreDrawListener(this)
                    val finalHeight = image.measuredHeight
                    val finalWidth = image.measuredWidth
                    Picasso.get().load("http://image.tmdb.org/t/p/w500/"+movie.posterPath)
                            .fit()
//                            .placeholder(R.drawable.placeholder)
//                            .centerCrop()
//                            .resize(finalWidth, finalHeight)
                            .into(image, object : Callback{
                                override fun onSuccess() {
                                    mLVBlock.stopAnim()
                                    mLVBlock.visibility = View.GONE
                                }

                                override fun onError(e: Exception?) {
                                }

                            })

                    return true
                }
            })
        }

        private fun getColor(pos: Int): String {
            val colors = arrayOf("#b871b3", "#530852", "#091256", "#094e56", "#095613", "#305609", "#565209",
                    "#561c09", "#00bcd4", "#03a9f4", "#ff9800", "#607d8b", "#ec407a", "#d4e157", "#e84e40")
            return colors[pos]
        }
    }
}