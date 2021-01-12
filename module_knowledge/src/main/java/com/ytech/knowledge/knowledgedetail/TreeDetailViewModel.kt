package com.ytech.knowledge.knowledgedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.hjq.toast.ToastUtils
import com.ytech.core.net.model.NetResult
import com.ytech.model.DatasBean
import kotlinx.coroutines.launch
import java.util.*


class TreeDetailViewModel: ViewModel() {

    private var dataSource: DataSource<Int, DatasBean>? = null
    fun getDataSource(): DataSource<Int, DatasBean>? {
        return dataSource
    }

    private var pageData: LiveData<PagedList<DatasBean>>
    fun getPageData(): LiveData<PagedList<DatasBean>> {
        return pageData
    }

    private var mCid: Int = 0

    fun setCid(cid: Int) {
        mCid = cid
    }


    init {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .build()

        val factory = object : DataSource.Factory<Int, DatasBean>() {
            override fun create(): DataSource<Int, DatasBean> {
                //使用 || 或者会导致刷新的时候 create会被不停的调用
                if (dataSource == null || dataSource?.isInvalid != false) {
                    dataSource = createDataSource()
                }
                return dataSource!!
            }
        }
        pageData = LivePagedListBuilder(factory, config).build()
    }

    fun createDataSource(): DataSource<Int, DatasBean> {
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