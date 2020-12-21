package com.ytech.common.common

fun getFitCenterSize(width: Int, height: Int, contentWidth: Int, contentHeight: Int): IntArray {
    val imgWidth: Int
    val imgHeight: Int
    if (contentWidth * 1f / width - contentHeight * 1f / height > 0) {
        imgHeight = contentHeight
        imgWidth = (width * imgHeight) / height
    } else {
        imgWidth = contentWidth
        imgHeight = (height * imgWidth) / width
    }
    return intArrayOf(imgWidth, imgHeight)
}

fun getFitImageViewSize(destWidth: Int, destHeight: Int, width: Int, maxHeight: Int): IntArray {
    if (destWidth <= 0 || destHeight <= 0) return intArrayOf(width, width)
    val destWidthHeightRatio = destWidth * 1f / destHeight
    val height = width / destWidthHeightRatio
    return if (height > maxHeight) intArrayOf(width, maxHeight) else intArrayOf(width, height.toInt())
}