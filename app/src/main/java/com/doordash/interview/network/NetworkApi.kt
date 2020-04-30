package com.doordash.interview.network

import com.doordash.interview.list.ListItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

//    @GET("/list")
//    fun fetchItems(): Call<List<NetworkItem>>

    @GET(value = "/v2/restaurant/")
    fun fetchItems(@Query("lat") latitude: Double, @Query("lng") longitude: Double,  @Query("offset") offset: Int? = null ,  @Query("limit") limit: Int? = null): Call<List<ListItem>>
/*
    @GET("/comments")
    fun fetchDataById(@Query("postId") id: Long): Call<List<NetworkItem>>
*/}