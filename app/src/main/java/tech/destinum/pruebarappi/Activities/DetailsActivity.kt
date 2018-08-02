package tech.destinum.pruebarappi.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ldoublem.loadingviewlib.view.LVBlock
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import tech.destinum.pruebarappi.R
import tech.destinum.pruebarappi.Repository.Local.Entities.Movie
import java.lang.Exception


class DetailsActivity : AppCompatActivity() {


    private lateinit var title: TextView
    private lateinit var rating: TextView
    private lateinit var released: TextView
    private lateinit var overview: TextView
    private lateinit var image: ImageView
    private lateinit var mLVBlock: LVBlock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Details")

        title = findViewById(R.id.details_title)
        rating = findViewById(R.id.details_rating)
        released = findViewById(R.id.details_released)
        overview = findViewById(R.id.details_overview)
        image = findViewById(R.id.details_image)
        mLVBlock = findViewById(R.id.lvBlock2)


        val movie = intent.extras.getParcelable("movie") as Movie

        title.text = movie.title
        rating.text = movie.voteAverage.toString()
        overview.text = movie.overview
        released.text = movie.releaseDate
        movie.category

        mLVBlock.visibility = View.VISIBLE
        mLVBlock.isShadow(false)
        mLVBlock.startAnim()

        Picasso.get().load("http://image.tmdb.org/t/p/w500/"+movie.posterPath)
                .fit()
//                .placeholder(R.drawable.placeholder)
                .into(image, object : Callback {
                    override fun onSuccess() {
                        mLVBlock.stopAnim()
                        mLVBlock.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                    }

                })

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home ->  {
                onBackPressed()
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
