package com.doordash.interview

import android.app.Application
import com.doordash.interview.db.ItemRoomDatabase
import com.doordash.interview.list.DataFetcher
import com.doordash.interview.network.NetworkService
import java.util.concurrent.Executors

class DoorDashApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // initialize loading before view is visible
        getRepository();
    }

    fun getRepository(): DataFetcher {
        return DataFetcher(ItemRoomDatabase.getDatabase(this), NetworkService().api, Executors.newCachedThreadPool())
    }
}