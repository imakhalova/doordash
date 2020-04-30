package com.doordash.interview.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService {
    private var retrofit = Retrofit.Builder()
        .baseUrl("https://api.doordash.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var api: NetworkApi = retrofit.create(NetworkApi::class.java)

}