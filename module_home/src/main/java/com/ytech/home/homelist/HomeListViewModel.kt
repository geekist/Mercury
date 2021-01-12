package com.ytech.home.homelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.hjq.toast.ToastUtils
import com.ytech.model.DatasBean
import com.ytech.core.net.model.NetResult
import kotlinx.coroutines.launch
import java.util.*

class HomeListViewModel : ViewModel() {

    private var dataSource: DataSource<Int, com.ytech.model.DatasBean>? = null
    private var pagedListData: LiveData<PagedList<com.ytech.model.DatasBean>>

    fun getDataSource(): DataSource<Int, com.ytech.model.DatasBean>? {
        return dataSource
    }

    fun getPageData(): LiveData<PagedList<com.ytech.model.DatasBean>> {
        return pagedListData
    }

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .build()

        val factory = object : DataSource.Factory<Int, com.ytech.model.DatasBean>() {
            override fun create(): DataSource<Int, com.ytech.model.DatasBean> {
                //使用 || 或者会导致刷新的时候 create会被不停的调用
                if (dataSource == null || dataSource?.isInvalid != false) {
                    dataSource = createDataSource()
                }
                return dataSource!!
            }
        }

        pagedListData = LivePagedListBuilder<Int, com.ytech.model.DatasBean>(factory, config).build()
    }


    private fun createDataSource(): DataSource<Int, com.ytech.model.DatasBean> {

        return object : PageKeyedDataSource<Int, com.ytech.model.DatasBean>() {
            override fun loadInitial(
                params: LoadInitialParams<Int>,
                callback: LoadInitialCallback<Int, com.ytech.model.DatasBean>
            ) {
                getHomeList(0, callback)
            }

            override fun loadAfter(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, com.ytech.model.DatasBean>
            ) {
                getHomeList(params.key, params.key + 1, callback)
            }

            override fun loadBefore(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, com.ytech.model.DatasBean>
            ) {
                callback.onResult(Collections.emptyList(), null)
            }
        }
    }

    fun getHomeList(
        count: Int,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, com.ytech.model.DatasBean>
    ) {
        viewModelScope.launch {
            when(val homeFeed = HomeListRepository.getHomeList(count)){
                is NetResult.Success ->callback.onResult(homeFeed.data.datas, null, 1)
                is NetResult.Error -> ToastUtils.show(homeFeed.exception.msg)
            }
        }
    }

    fun getHomeList(
        count: Int,
        key: Int,
        callback: PageKeyedDataSource.LoadCallback<Int, com.ytech.model.DatasBean>
    ) {
        viewModelScope.launch {
            when(val homeFeed = HomeListRepository.getHomeList(count)){
                is NetResult.Success ->callback.onResult(homeFeed.data.datas, key)
                is NetResult.Error ->ToastUtils.show(homeFeed.exception.msg)
            }
        }
    }
}