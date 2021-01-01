package com.ytech.apply.applydetail

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.ytech.apply.applydetail.model.ProjectItemSub
import androidx.lifecycle.viewModelScope
import com.ytech.core.model.BaseContext
import com.ytech.core.net.model.NetResult
import kotlinx.coroutines.launch

import java.util.*

class ApplyItemViewModel() : ViewModel() {

    private var mId: Int = 0

    fun setId(id: Int) {
        mId = id
    }

    private var dataSource: DataSource<Int, ProjectItemSub>? = null
    private var pageData: LiveData<PagedList<ProjectItemSub>>

    fun getPageData(): LiveData<PagedList<ProjectItemSub>> {
        return pageData
    }

    fun getDataSource(): DataSource<Int, ProjectItemSub>? {
        return dataSource
    }

    init {

        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .build()

        val factory = object : DataSource.Factory<Int, ProjectItemSub>() {
            override fun create(): DataSource<Int, ProjectItemSub> {
                //使用 || 或者会导致刷新的时候 create会被不停的调用
                if (dataSource == null || dataSource?.isInvalid != false) {
                    dataSource = createDataSource()
                }
                return dataSource!!
            }

        }

        pageData = LivePagedListBuilder<Int, ProjectItemSub>(factory, config).build()
    }

    fun createDataSource(): DataSource<Int, ProjectItemSub> {

        return object : PageKeyedDataSource<Int, ProjectItemSub>() {
            override fun loadInitial(
                params: LoadInitialParams<Int>,
                callback: LoadInitialCallback<Int, ProjectItemSub>
            ) {
                getTabPageData(1, callback)
            }

            override fun loadAfter(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, ProjectItemSub>
            ) {
                getTabPageData(params.key, callback)
            }

            override fun loadBefore(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, ProjectItemSub>
            ) {
                callback.onResult(Collections.emptyList(), null)
            }

        }
    }

    fun getTabPageData(
        count: Int,
        callback: PageKeyedDataSource.LoadCallback<Int, ProjectItemSub>
    ) {
        viewModelScope.launch {
            val data = ApplyItemRepository.getTabItemPageData(count, mId)
            if (data is NetResult.Success) {
                callback.onResult(data.data.datas, count + 1)
            } else if (data is NetResult.Error) {
                Toast.makeText(
                    BaseContext.instance.getContext(),
                    data.exception.msg,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun getTabPageData(
        count: Int,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, ProjectItemSub>
    ) {
        viewModelScope.launch {
            val data = ApplyItemRepository.getTabItemPageData(count, mId)
            if (data is NetResult.Success) {
                callback.onResult(data.data.datas, 0, 2)
            } else if (data is NetResult.Error) {
                Toast.makeText(
                    BaseContext.instance.getContext(),
                    data.exception.msg,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}