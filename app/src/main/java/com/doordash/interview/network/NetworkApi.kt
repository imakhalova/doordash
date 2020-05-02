package com.doordash.interview.network

import com.doordash.interview.detail.DetailItem
import com.doordash.interview.list.ListItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface that describes API to backend
 */
interface NetworkApi {

    @GET(value = "/v2/restaurant/")
    fun fetchItems(@Query("lat") latitude: Double, @Query("lng") longitude: Double,  @Query("offset") offset: Int? = null ,  @Query("limit") limit: Int? = null): Call<List<ListItem>>

    @GET(value = "/v2/restaurant/{id}")
    fun fetchItemDetail(@Path("id") id: Int) : Call<DetailItem>
}