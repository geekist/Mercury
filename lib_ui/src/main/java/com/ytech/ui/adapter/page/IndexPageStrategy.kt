package com.ytech.ui.adapter.page


class IndexPageStrategy<ITEM : Any> : IPageStrategy<ITEM> {
    private var mInitializePage = 0
    private var mPage = mInitializePage

    override fun pageStrategy(list: List<ITEM>, isRefresh: Boolean) {
        if (list.isNullOrEmpty()) return
        if (isRefresh) mPage = mInitializePage + 1 else mPage += 1
    }

    fun setInitializePage(initializePage: Int) {
        mInitializePage = initializePage
        mPage = mInitializePage
    }

    fun getPage(isRefresh: Boolean = true) = if (isRefresh) mInitializePage else mPage
}