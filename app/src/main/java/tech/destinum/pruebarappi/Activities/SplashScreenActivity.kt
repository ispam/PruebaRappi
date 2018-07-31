package tech.destinum.pruebarappi.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ldoublem.loadingviewlib.view.LVGhost
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import tech.destinum.pruebarappi.BuildConfig
import tech.destinum.pruebarappi.R
import java.util.concurrent.TimeUnit

class SplashScreenActivity : AppCompatActivity() {

    private var mSP: SharedPreferences? = null
    private lateinit var mLVGhost: LVGhost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mLVGhost = findViewById(R.id.lvghost)
        mLVGhost.startAnim()

        checkFirstRun()
    }

    private fun checkFirstRun() {

        val PREF_NAME = "SplashPrefs"
        val PREF_VERSION_CODE_KEY = "1"
        val DOESNT_EXIST = -1

        // Get current version code
        val currentVersionCode = BuildConfig.VERSION_CODE

        // Get saved version code
        mSP = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedVersionCode = mSP!!.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST)

        // Check for first run or upgrade
        when {
            currentVersionCode == savedVersionCode ->{
                // This is just a normal run

                goHomeActivity()
            }

            savedVersionCode == DOESNT_EXIST -> {
                // TODO This is a new install (or the user cleared the shared preferences)

                // TODO get lots of movies
                goHomeActivity()

//                createMatchdays()
//                goTutorial()

            }

            currentVersionCode > savedVersionCode -> {
                // TODO This is an upgrade

            }
        }

        mSP!!.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply()

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
