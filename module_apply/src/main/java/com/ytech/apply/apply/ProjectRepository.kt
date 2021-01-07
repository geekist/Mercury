package com.ytech.apply.apply

import com.ytech.apply.apply.api.ApplyApiService
import com.ytech.core.net.model.NetResult
import com.ytech.core.net.net.BaseRepository
import com.ytech.core.net.net.RetrofitClient
import com.ytech.model.apply.ProjectTabItem

object ProjectRepository : BaseRepository() {

    suspend fun getTabData(): NetResult<MutableList<ProjectTabItem>> {
        return requestResponse(api = { RetrofitClient.create<ApplyApiService>().getTabData() })
    }
}