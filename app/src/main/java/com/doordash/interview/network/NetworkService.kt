package com.doordash.interview.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Set up connection to server and provides implementation of network APIs
 */
class NetworkService {
    private var retrofit = Retrofit.Builder()
        .baseUrl(SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var api: NetworkApi = retrofit.create(NetworkApi::class.java)

    companion object {
        const val SERVER_URL = "https://api.doordash.com/";
    }
}