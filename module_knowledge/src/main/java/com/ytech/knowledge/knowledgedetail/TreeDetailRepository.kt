package com.ytech.knowledge.knowledgedetail

import com.ytech.core.net.model.NetResult
import com.ytech.core.net.net.BaseRepository
import com.ytech.core.net.net.RetrofitClient
import com.ytech.knowledge.knowledgedetail.api.KnowledgeDetailApiService
import com.ytech.knowledge.knowledgedetail.model.TreeDetailModel


object TreeDetailRepository : BaseRepository() {

    suspend fun getTreeDetailList(count: Int, cid: Int): NetResult<TreeDetailModel> {
        return requestResponse(api = {RetrofitClient.create<KnowledgeDetailApiService>().getTreeDetailList(count, cid)})
    }
}