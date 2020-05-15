package com.doordash.interview.list

import androidx.lifecycle.*
import com.doordash.interview.arch.SingleLiveEvent
import com.doordash.interview.db.FavStorage
import com.doordash.interview.db.IFavStorage
import com.doordash.interview.detail.DetailItem

/**
 * View model for fragments that need to operate with items received from backend
 */
class MainViewModel(private val fetcher: DataFetcher, private val favStorage: IFavStorage)
    : ViewModel(){

    private val _favItems = MutableLiveData<ListItem>()
    val listItems: LiveData<List<ListItem>> = MediatorLiveData<List<ListItem>>().apply {
        addSource(fetcher.items) {
            it?.forEach {
                    item -> item.isFav = favStorage.contains(item)
            }
            value = it
        }
        addSource(_favItems) {
            value = value
        }
    }

    var selectedItem = SingleLiveEvent<ListItem>()

    fun refresh() {
        fetcher.fetchFromNetwork();
    }

    fun getDetails(item: ListItem) : LiveData<DetailItem> {
        return fetcher.getDetails(item)
    }

    fun updateFavourite(item: ListItem) {
        favStorage.switchState(item)
        item.isFav = favStorage.contains(item)
        // notify live data about changes, so it updates UI to reflect change
        _favItems.value = _favItems.value
    }
}
