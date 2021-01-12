package com.ytech.home.homelist

import com.ytech.core.net.model.NetResult
import com.ytech.core.net.net.BaseRepository
import com.ytech.core.net.net.RetrofitClient
import com.ytech.home.homelist.api.HomeListApiService
import com.ytech.model.home.DataFeed


object HomeListRepository : BaseRepository() {

    suspend fun getHomeList(count: Int): NetResult<com.ytech.model.home.DataFeed> {
        return requestResponse(api = { RetrofitClient.create<HomeListApiService>().getHomeList(count)})
    }
}