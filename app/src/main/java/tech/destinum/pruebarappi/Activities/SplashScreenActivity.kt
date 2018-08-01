package tech.destinum.pruebarappi.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ldoublem.loadingviewlib.view.LVGhost
import io.reactivex.Completable
import tech.destinum.pruebarappi.R
import java.util.concurrent.TimeUnit

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var mLVGhost: LVGhost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mLVGhost = findViewById(R.id.lvghost)
        mLVGhost.startAnim()

        goHomeActivity()
    }

    private fun goHomeActivity(){
        Completable.timer(3500, TimeUnit.MILLISECONDS)
                .doOnComplete {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }.subscribe()
    }
}
