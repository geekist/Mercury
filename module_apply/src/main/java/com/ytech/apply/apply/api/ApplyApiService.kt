package com.ytech.apply.apply.api

import com.ytech.apply.apply.model.ProjectTabItem
import com.ytech.core.net.model.BaseModel
import retrofit2.http.GET

interface ApplyApiService {

    @GET("/project/tree/json")
    suspend fun getTabData(): BaseModel<MutableList<ProjectTabItem>>

}