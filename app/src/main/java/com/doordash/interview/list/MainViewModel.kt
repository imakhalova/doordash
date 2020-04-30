package com.doordash.interview.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doordash.interview.arch.SingleLiveEvent

/**
 * View model for fragments that need to operate with items received from backend
 */
class MainViewModel(private val fetcher: DataFetcher) : ViewModel(){

    val listItems: LiveData<List<ListItem>> by lazy {
        fetcher.items
    }

    var selectedItem = SingleLiveEvent<ListItem>()

    fun refresh() {
        fetcher.fetchFromNetwork();
    }
}
