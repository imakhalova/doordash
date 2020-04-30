package com.doordash.interview.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.doordash.interview.list.ListItem

/**
 * Interface that describes API to access local cache
 */
@Dao
abstract class NetworkItemDao {

    @Transaction
    open fun updateAll(item: List<ListItem>) {
        deleteAll()
        insertAll(item)
    }

    @Query("SELECT * from `table`")
    abstract fun allData(): LiveData<List<ListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(item: List<ListItem>)

    @Query("DELETE FROM `table`")
    abstract fun deleteAll()
}