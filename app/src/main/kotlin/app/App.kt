package app

import android.app.Application
import android.content.Context
import co.appreactor.news.BuildConfig
import co.appreactor.news.R
import db.db
import org.acra.config.dialog
import org.acra.config.mailSender
import org.acra.data.StringFormat
import org.acra.ktx.initAcra
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        initAcra {
            buildConfigClass = BuildConfig::class.java
            reportFormat = StringFormat.JSON

            mailSender {
                mailTo = getString(R.string.crash_report_email)
            }

            dialog {
                resTheme = R.style.Theme_Material3_DynamicColors_DayNight
                resIcon = null
                title = getString(R.string.crash_title)
                text = getString(R.string.crash_summary)
                positiveButtonText = getString(R.string.share)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.DEBUG)
            androidContext(this@App)
            defaultModule()
            modules(module { single { db(this@App) } })
        }
    }
}