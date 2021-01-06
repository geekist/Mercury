package com.ytech.knowledge.knowledgedetail

import com.ytech.core.net.model.NetResult
import com.ytech.core.net.net.BaseRepository
import com.ytech.core.net.net.RetrofitClient
import com.ytech.knowledge.knowledgedetail.api.KnowledgeDetailApiService
import com.ytech.model.knowledge.TreeDetailModel


object TreeDetailRepository : BaseRepository() {

    suspend fun getTreeDetailList(count: Int, cid: Int): NetResult<com.ytech.model.knowledge.TreeDetailModel> {
        return requestResponse(api = {RetrofitClient.create<KnowledgeDetailApiService>().getTreeDetailList(count, cid)})
    }
}