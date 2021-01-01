package com.ytech.home.homelist.model

import com.ytech.core.model.DatasBean

data class DataFeed(
    val curPage: Int,
    val offset: Int,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val over: Boolean,
    val datas:MutableList<DatasBean>
    )