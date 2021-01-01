package com.ytech.knowledge.knowledgedetail

import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.hjq.toast.ToastUtils
import com.ytech.core.GlobalConfig
import com.ytech.core.model.DatasBean
import com.ytech.core.net.model.NetResult
import kotlinx.coroutines.launch
import java.util.*


class TreeDetailViewModel() :
    AbsListViewModel<DatasBean>() {

    private var mCid: Int = 0

    fun setCid(cid: Int) {
        mCid = cid
    }

    override fun createDataSource(): DataSource<Int, DatasBean> {

        return object : PageKeyedDataSource<Int, DatasBean>() {
            override fun loadInitial(
                params: LoadInitialParams<Int>,
                callback: LoadInitialCallback<Int, DatasBean>
            ) {
                loadData(0, callback)
            }

            override fun loadAfter(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, DatasBean>
            ) {
                loadData(params.key, callback)
            }

            override fun loadBefore(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, DatasBean>
            ) {
                callback.onResult(Collections.emptyList(), null)
            }
        }

    }

    private fun loadData(
        key: Int,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, DatasBean>
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
        callback: PageKeyedDataSource.LoadCallback<Int, DatasBean>
    ) {
        viewModelScope.launch {
            when(val treeDetail = TreeDetailRepository.getTreeDetailList(key, mCid)){
                is NetResult.Success ->  callback.onResult(treeDetail.data.datas, key + 1)
                is NetResult.Error -> ToastUtils.show(treeDetail.exception.msg)
            }
        }
    }
}