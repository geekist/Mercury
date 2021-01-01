package com.ytech.apply.apply

import com.ytech.apply.apply.api.ApplyApiService
import com.ytech.apply.apply.model.ProjectTabItem
import com.ytech.core.net.model.NetResult
import com.ytech.core.net.net.BaseRepository
import com.ytech.core.net.net.RetrofitClient


object ProjectRepository : BaseRepository() {

    suspend fun getTabData(): NetResult<MutableList<ProjectTabItem>> {
        return requestResponse(api = { RetrofitClient.create<ApplyApiService>().getTabData() })
    }
}