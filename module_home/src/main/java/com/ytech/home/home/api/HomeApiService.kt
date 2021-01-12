package com.ytech.home.home.api


import com.ytech.core.net.model.BaseModel
import com.ytech.model.home.Banner
import retrofit2.http.GET

/**
 * Create by liwen on 2020-05-18
 */
interface HomeApiService {

    @GET("/banner/json")
    suspend fun getBanner(): BaseModel<MutableList<com.ytech.model.home.Banner>>
}