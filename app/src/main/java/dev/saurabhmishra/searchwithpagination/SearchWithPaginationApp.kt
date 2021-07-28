package dev.saurabhmishra.searchwithpagination

import android.app.Application
import dev.saurabhmishra.searchwithpagination.injection.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SearchWithPaginationApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SearchWithPaginationApp)
            modules(appModule)
        }
    }
}