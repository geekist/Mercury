package com.ytech.home.homelist.api


import com.ytech.core.net.model.BaseModel
import com.ytech.model.home.DataFeed
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeListApiService {

    @GET("/article/list/{count}/json")
    suspend fun getHomeList(@Path("count") count: Int): BaseModel<com.ytech.model.home.DataFeed>
}