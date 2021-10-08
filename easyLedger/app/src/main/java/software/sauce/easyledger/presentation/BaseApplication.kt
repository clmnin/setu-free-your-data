package software.sauce.easyledger.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import software.sauce.easyledger.utils.Prefs

val prefs: Prefs by lazy {
    BaseApplication.prefs!!
}

@HiltAndroidApp
class BaseApplication : Application() {
    companion object {
        var prefs: Prefs? = null
        lateinit var instance: BaseApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        prefs = Prefs(applicationContext)
    }
}