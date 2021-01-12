package com.ytech.model.knowledge

data class TreeData(
    val children: MutableList<TreeDataItem>,
    val name: String,
    val id: Int,
    val visible: Int
)