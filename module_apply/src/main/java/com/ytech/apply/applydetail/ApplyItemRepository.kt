package com.ytech.apply.applydetail

import com.ytech.model.apply.ApplyPageItem
import com.ytech.apply.applydetail.api.ApplyItemApiService
import com.ytech.core.net.model.NetResult
import com.ytech.core.net.net.BaseRepository
import com.ytech.core.net.net.RetrofitClient


object ApplyItemRepository : BaseRepository() {

    suspend fun getTabItemPageData(count: Int, id: Int): NetResult<com.ytech.model.apply.ApplyPageItem> {
        return requestResponse(api = {RetrofitClient.create<ApplyItemApiService>().getTabItemPageData(count, id)})
    }
}