package com.ytech.knowledge.knowledge.api


import com.ytech.core.net.model.BaseModel
import com.ytech.model.knowledge.TreeData
import retrofit2.http.GET

/**
 * Create by liwen on 2020-05-18
 */
interface KnowledgeApiService {
/*
    @GET("/banner/json")
    suspend fun getBanner(): BaseModel<MutableList<Banner>>

    @GET("/article/list/{count}/json")
    suspend fun getHomeList(@Path("count") count: Int): BaseModel<DataFeed>
*/
    @GET("/tree/json")
    suspend fun getTreeList(): BaseModel<MutableList<com.ytech.model.knowledge.TreeData>>
/*
    @GET("/project/tree/json")
    suspend fun getTabData(): BaseModel<MutableList<ProjectTabItem>>

    @GET("/project/list/{count}/json")
    suspend fun getTabItemPageData(
        @Path("count") count: Int,
        @Query("cid") cid: Int
    ): BaseModel<ProjectPageItem>


    @GET("/navi/json")
    suspend fun getNavigationData(): BaseModel<MutableList<NavigationItem>>
*/
}