package com.doordash.interview.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.doordash.interview.list.ListItem


@Dao
abstract class NetworkItemDao {
//    @Query("SELECT * from `table` ORDER BY status ASC LIMIT :limit OFFSET :offset")
//    fun readPage(limit: Int? = null, offset: Int? = null): LiveData<List<NetworkItem>>

    @Transaction
    open fun updateAll(item: List<ListItem>) {
        // Anything inside this method runs in a single transaction.
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