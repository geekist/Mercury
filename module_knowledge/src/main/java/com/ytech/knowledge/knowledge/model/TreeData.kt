package com.ytech.knowledge.knowledge.model

data class TreeData(
    val children: MutableList<TreeDataItem>,
    val name: String,
    val id: Int,
    val visible: Int
)