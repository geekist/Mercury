package com.ytech.knowledge.knowledgedetail.api

import com.ytech.core.net.model.BaseModel
import com.ytech.model.knowledge.TreeDetailModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KnowledgeDetailApiService {
    @GET(value = "/article/list/{count}/json")
    suspend fun getTreeDetailList(
        @Path("count") count: Int,
        @Query("cid") cid: Int
    ): BaseModel<TreeDetailModel>
}