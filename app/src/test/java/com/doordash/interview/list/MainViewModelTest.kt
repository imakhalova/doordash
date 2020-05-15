package com.doordash.interview.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.doordash.interview.db.IFavStorage
import com.doordash.interview.db.ItemRoomDatabase
import com.doordash.interview.db.NetworkItemDao
import com.doordash.interview.detail.DetailItem
import com.doordash.interview.network.NetworkApi
import okhttp3.Request
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class MainViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val executor = ThreadExecutorMock()

    @Test
    fun readFromDb() {
        val latch = CountDownLatch(1)

        val db = ItemRoomDatabaseMock()
        val fetcher = DataFetcher(db, NetworkApiMock(RetrofitCallSuccessMock(LinkedList<ListItem>())), executor)
        val viewModel = MainViewModel(fetcher, Mockito.mock(IFavStorage::class.java))
        val mockCache = LinkedList<ListItem>();
        mockCache.add(
            ListItem(
                0,
                0,
                "mockCache",
                "description",
                "https://logo",
                "30 min"
            )
        );

        viewModel.listItems.observeOnce(Observer<List<ListItem>> {
            Assert.assertEquals(mockCache.size, it.size)
            Assert.assertEquals(mockCache[0].name, it[0].name)
            Assert.assertEquals(mockCache[0].status, it[0].status)
            latch.countDown()
        })

        db.wordDao().insertAll(mockCache)

        // wait async calls to be completed (live data is set)
        if (!latch.await(1000, TimeUnit.MILLISECONDS)) {
            Assert.fail("LiveData value was never set.")
        }

    }

    @Test
    fun readFromNetwork() {
        val latch = CountDownLatch(1)

        val networkItems = LinkedList<ListItem>();
        networkItems.add(
            ListItem(
                0,
                0,
                "networkItems",
                "description",
                "https://logo",
                "20 min"
            )
        );

        val fetcher = DataFetcher(ItemRoomDatabaseMock(), NetworkApiMock(RetrofitCallSuccessMock(networkItems)), executor)
        val viewModel = MainViewModel(fetcher, Mockito.mock(IFavStorage::class.java))

        viewModel.listItems.observeOnce(Observer<List<ListItem>> {
            Assert.assertEquals(networkItems.size, it.size)
            Assert.assertEquals(networkItems[0].name, it[0].name)
            latch.countDown()
        })
        viewModel.refresh()

        // wait async calls to be completed (live data is set)
        if (!latch.await(1000, TimeUnit.MILLISECONDS)) {
            Assert.fail("LiveData value was never set.")
        }
    }

    @Test
    fun networkDataOverridesCache() {
        val latch = CountDownLatch(2)

        val mockCache = LinkedList<ListItem>();
        mockCache.add(
            ListItem(
                0,
                0,
                "mockCache",
                "description",
                "https://logo",
                "30 min"
            )
        );
        val db = ItemRoomDatabaseMock()
        db.wordDao().insertAll(mockCache)

        val networkItems = LinkedList<ListItem>();
        networkItems.add(
            ListItem(
                0,
                0,
                "networkItems",
                "description",
                "https://logo",
                "20 min"
            )
        );


        val fetcher = DataFetcher(db, NetworkApiMock(RetrofitCallSuccessMock(networkItems)), executor)
        val viewModel = MainViewModel(fetcher, Mockito.mock(IFavStorage::class.java))

        viewModel.refresh()
        viewModel.listItems.observeOnce(Observer<List<ListItem>> {
            // changes from cache
            latch.countDown()
            Assert.assertEquals(mockCache.size, viewModel.listItems.value?.size)
            Assert.assertEquals(mockCache[0].name, viewModel.listItems.value?.get(0)?.name)
        })
        viewModel.listItems.observeOnce(Observer<List<ListItem>> {
            // changes from network
            Assert.assertEquals(networkItems.size, viewModel.listItems.value?.size)
            Assert.assertEquals(networkItems[0].name, viewModel.listItems.value?.get(0)?.name)
            latch.countDown()
        })

        if (!latch.await(2000, TimeUnit.MILLISECONDS)) {
            Assert.fail("LiveData value was never set.")
        }
    }

    @Test
    fun failNetwork() {
        val latch = CountDownLatch(1)

        val fetcher = DataFetcher(ItemRoomDatabaseMock(), NetworkApiMock(RetrofitCallFailure()), executor)
        val viewModel = MainViewModel(fetcher, Mockito.mock(IFavStorage::class.java))

        viewModel.listItems.observeOnce(Observer<List<ListItem>> {
            latch.countDown()
        })
        viewModel.refresh()

        if (!latch.await(2000, TimeUnit.MILLISECONDS)) {
            Assert.fail("LiveData value was never set.")
        }

        Assert.assertNull(viewModel.listItems.value)

    }

    private fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    class ThreadExecutorMock : Executor {
        override fun execute(r: Runnable) {
            r.run()
        }
    }

    class NetworkApiMock(private val call: Call<List<ListItem>>) : NetworkApi {
        override fun fetchItems(
            latitude: Double,
            longitude: Double,
            offset: Int?,
            limit: Int?
        ): Call<List<ListItem>> {
            return call
        }

        override fun fetchItemDetail(id: Int): Call<DetailItem> {
            TODO("Not yet implemented")
        }
    }

    class RetrofitCallSuccessMock(private val items: List<ListItem>) : RetrofitCallMock() {
        override fun execute(): Response<List<ListItem>> {
            return Response.success(items)
        }
    }

    class RetrofitCallFailure() : RetrofitCallMock() {
        override fun execute(): Response<List<ListItem>> {
            return Response.error(400, Mockito.mock(ResponseBody::class.java))
        }
    }

    abstract class RetrofitCallMock() : Call<List<ListItem>> {
        override fun enqueue(callback: Callback<List<ListItem>>) {
            callback.onResponse(this, execute())
        }

        override fun isExecuted(): Boolean {
            TODO("Not yet implemented")
        }

        override fun clone(): Call<List<ListItem>> {
            TODO("Not yet implemented")
        }

        override fun isCanceled(): Boolean {
            TODO("Not yet implemented")
        }

        override fun cancel() {
            TODO("Not yet implemented")
        }

        override fun request(): Request {
            TODO("Not yet implemented")
        }
    }


    class NetworkItemDaoMock() : NetworkItemDao() {
        val items = MutableLiveData<List<ListItem>>()

        override fun allData(): LiveData<List<ListItem>> {
            return items;
        }

        override fun insertAll(item: List<ListItem>) {
            items.value = item
        }

        override fun deleteAll() {
            val list = items.value?.toMutableList()
            list?.clear()
            items.value = list
        }
    }

    class ItemRoomDatabaseMock() : ItemRoomDatabase() {
        private val dao = NetworkItemDaoMock()

        override fun wordDao(): NetworkItemDao {
            return dao;
        }

        override fun runInTransaction(body: Runnable) {
            // Whenever runInTransaction is called we take the Runnable argument and run it
            body.run()
        }

        override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
            return Mockito.mock(SupportSQLiteOpenHelper::class.java)
        }

        override fun createInvalidationTracker(): InvalidationTracker {
            return Mockito.mock(InvalidationTracker::class.java)
        }

        override fun clearAllTables() {
        }
    }
}