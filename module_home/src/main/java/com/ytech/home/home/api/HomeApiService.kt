package com.ytech.home.home.api


import com.ytech.core.net.model.BaseModel
import com.ytech.home.home.model.Banner
import com.ytech.home.homelist.model.DataFeed
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Create by liwen on 2020-05-18
 */
interface HomeApiService {

    @GET("/banner/json")
    suspend fun getBanner(): BaseModel<MutableList<Banner>>
}