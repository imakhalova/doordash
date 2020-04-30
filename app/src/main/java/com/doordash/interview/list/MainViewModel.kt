package com.doordash.interview.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * View model for fragments that need to operate with items received from backend
 */
class MainViewModel(private val fetcher: DataFetcher) : ViewModel(){

    val listItems: LiveData<List<ListItem>> by lazy {
        fetcher.items
    }

    var selectedItem: MutableLiveData<ListItem> = MutableLiveData<ListItem>()

    fun refresh() {
        fetcher.fetchFromNetwork();
    }
}
