package com.ytech.knowledge.knowledgedetail

import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.hjq.toast.ToastUtils
import com.ytech.model.DatasBean
import com.ytech.core.net.model.NetResult
import kotlinx.coroutines.launch
import java.util.*


class TreeDetailViewModel() :
    AbsListViewModel<com.ytech.model.DatasBean>() {

    private var mCid: Int = 0

    fun setCid(cid: Int) {
        mCid = cid
    }

    override fun createDataSource(): DataSource<Int, com.ytech.model.DatasBean> {

        return object : PageKeyedDataSource<Int, com.ytech.model.DatasBean>() {
            override fun loadInitial(
                params: LoadInitialParams<Int>,
                callback: LoadInitialCallback<Int, com.ytech.model.DatasBean>
            ) {
                loadData(0, callback)
            }

            override fun loadAfter(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, com.ytech.model.DatasBean>
            ) {
                loadData(params.key, callback)
            }

            override fun loadBefore(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, com.ytech.model.DatasBean>
            ) {
                callback.onResult(Collections.emptyList(), null)
            }
        }

    }

    private fun loadData(
        key: Int,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, com.ytech.model.DatasBean>
    ) {
        viewModelScope.launch {
            when(val treeDetail = TreeDetailRepository.getTreeDetailList(key, mCid)){
                is NetResult.Success -> callback.onResult(treeDetail.data.datas, null, 1)
                is NetResult.Error -> ToastUtils.show(treeDetail.exception.msg)
            }
        }
    }

    private fun loadData(
        key: Int,
        callback: PageKeyedDataSource.LoadCallback<Int, com.ytech.model.DatasBean>
    ) {
        viewModelScope.launch {
            when(val treeDetail = TreeDetailRepository.getTreeDetailList(key, mCid)){
                is NetResult.Success ->  callback.onResult(treeDetail.data.datas, key + 1)
                is NetResult.Error -> ToastUtils.show(treeDetail.exception.msg)
            }
        }
    }
}