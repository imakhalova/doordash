package com.doordash.interview.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.doordash.interview.db.ItemRoomDatabase
import com.doordash.interview.network.NetworkApi
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Executor


/**
 * Class that handling the work with network and db sources.
 */
class DataFetcher (private val mDatabase: ItemRoomDatabase, private val mApi: NetworkApi, private val executor: Executor) {
    private val mediatorLiveData: MediatorLiveData<List<ListItem>>

    /**
     * Get the list of items from the database and get notified when the data changes.
     */
    val items: LiveData<List<ListItem>>
        get() = mediatorLiveData

    val apiSource = MutableLiveData<List<ListItem>>();
    val dbSource = mDatabase.wordDao().allData()

    init {
        mediatorLiveData = MediatorLiveData<List<ListItem>>()


        mediatorLiveData.addSource(
                dbSource
                ) {
            if (it == null || it.isEmpty()) {
                fetchFromNetwork(FIRST_LOAD_SIZE);
            } else {
                // update only if we've got data initially
                // otherwise wait for network response
                mediatorLiveData.postValue(it)
            }
        }
        mediatorLiveData.addSource(apiSource) { response ->
            mediatorLiveData.postValue(response)
            executor.execute {
                mDatabase.runInTransaction {
                    response?.let {
                        mDatabase.wordDao().updateAll(it)
                    }
                }
            }
        }

    }

    fun fetchFromNetwork(limit: Int? = null) {
        // hardcoded numbers by task
        mApi.fetchItems(37.422740, -122.139956, 0, limit).enqueue(object : retrofit2.Callback<List<ListItem>> {
            override fun onFailure(call: Call<List<ListItem>>, t: Throwable) {
                // handle network errors
                // just placeholder to notify that operation ended so we don't spin the progress
                apiSource.value = apiSource.value
            }

            override fun onResponse(call: Call<List<ListItem>>, response: Response<List<ListItem>>) {
                apiSource.value = response.body();
            }

        });
    }

    companion object {
        const val FIRST_LOAD_SIZE = 50;
    }
}