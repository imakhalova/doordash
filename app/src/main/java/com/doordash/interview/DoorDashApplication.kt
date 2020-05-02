package com.doordash.interview

import android.app.Application
import com.doordash.interview.db.ItemRoomDatabase
import com.doordash.interview.list.DataFetcher
import com.doordash.interview.network.NetworkService
import java.util.concurrent.Executors

class DoorDashApplication : Application() {

    private val fetcher by lazy {
        DataFetcher(
                ItemRoomDatabase.getDatabase(this),
                NetworkService().api,
                Executors.newCachedThreadPool())
    }

    override fun onCreate() {
        super.onCreate()
        // initialize loading before view is visible
        getDataFetcher()
    }

    fun getDataFetcher(): DataFetcher {
        return fetcher
    }
}