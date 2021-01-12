package com.ytech.home.home

import com.ytech.core.net.model.NetResult
import com.ytech.core.net.net.BaseRepository
import com.ytech.core.net.net.RetrofitClient
import com.ytech.home.home.api.HomeApiService
import com.ytech.model.home.Banner


object HomeRepository : BaseRepository() {

    suspend fun getBanner(): NetResult<List<com.ytech.model.home.Banner>> {
        return requestResponse(api = {RetrofitClient.create<HomeApiService>().getBanner()})
    }
}