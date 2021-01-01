package com.ytech.ui.adapter.page

import com.ytech.common.common.Constants


interface IPageStrategy<ITEM : Any> {
    val pageSize: Int
        get() = Constants.DEFAULT_PAGE_SIZE

    fun pageStrategy(list: List<ITEM>, isRefresh: Boolean)
}