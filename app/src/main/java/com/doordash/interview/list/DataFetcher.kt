package com.doordash.interview.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.doordash.interview.db.ItemRoomDatabase
import com.doordash.interview.detail.DetailItem
import com.doordash.interview.network.NetworkApi
import retrofit2.Call
import retrofit2.Callback
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

    // 2 sources of data: one from backend and another from local cache
    private val apiSource = MutableLiveData<List<ListItem>>();
    private val dbSource = mDatabase.wordDao().allData()

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
        mApi.fetchItems(37.422740, -122.139956, 0, limit).enqueue(object : Callback<List<ListItem>> {
            override fun onFailure(call: Call<List<ListItem>>, t: Throwable) {
                handleError()
            }

            override fun onResponse(call: Call<List<ListItem>>, response: Response<List<ListItem>>) {
                if (response.isSuccessful) {
                    apiSource.value = response.body();
                } else {
                    handleError()
                }
            }

            fun handleError() {
                // here we need to handle network errors
                // right now it's just placeholder to notify that operation ended so we don't spin the progress
                mediatorLiveData.value = mediatorLiveData.value
                if (mediatorLiveData.value == null) {
                    mediatorLiveData.value = emptyList()
                }
            }

        })
    }

    fun getDetails(item: ListItem) : LiveData<DetailItem> {
        val details = MutableLiveData<DetailItem>()
        mApi.fetchItemDetail(item.id).enqueue( object : Callback<DetailItem> {
            override fun onFailure(call: Call<DetailItem>, t: Throwable) {

            }

            override fun onResponse(call: Call<DetailItem>, response: Response<DetailItem>) {
                if (response.isSuccessful) {
                    details.value = response.body()
                }
            }
        })
        return details
    }

    companion object {
        const val FIRST_LOAD_SIZE = 50;
    }
}