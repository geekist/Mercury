package com.ytech.apply.apply.api

import com.ytech.model.apply.ProjectTabItem
import com.ytech.core.net.model.BaseModel
import retrofit2.http.GET

interface ApplyApiService {

    @GET("/project/tree/json")
    suspend fun getTabData(): BaseModel<MutableList<com.ytech.model.apply.ProjectTabItem>>

}