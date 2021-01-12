package com.ytech.apply.applydetail.api

import com.ytech.model.apply.ApplyPageItem
import com.ytech.core.net.model.BaseModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApplyItemApiService {

    @GET("/project/list/{count}/json")
    suspend fun getTabItemPageData(
        @Path("count") count: Int,
        @Query("cid") cid: Int
    ): BaseModel<ApplyPageItem>
}