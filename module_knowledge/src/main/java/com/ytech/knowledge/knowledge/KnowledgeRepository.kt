package com.ytech.knowledge.knowledge

import com.ytech.core.net.model.NetResult
import com.ytech.core.net.net.BaseRepository
import com.ytech.core.net.net.RetrofitClient
import com.ytech.knowledge.knowledge.api.KnowledgeApiService
import com.ytech.model.knowledge.TreeData

object KnowledgeRepository : BaseRepository() {

    suspend fun getTreeList(): NetResult<MutableList<com.ytech.model.knowledge.TreeData>> {
        return requestResponse(api = {RetrofitClient.create<KnowledgeApiService>().getTreeList()})
    }
}