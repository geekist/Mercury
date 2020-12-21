package com.ytech.common.text

fun CharSequence?.isEmptyStr() = this == null || isEmpty()

fun CharSequence?.isNotEmptyStr() = this != null && isNotEmpty()

fun <R> CharSequence?.operate(ifNotEmpty: (str: CharSequence) -> R, ifEmpty: () -> R): R =
    if (this.isNullOrEmpty()) {
        ifEmpty()
    } else {
        ifNotEmpty(this)
    }

/**
 * 18804655646 -> 188****5646
 */
fun CharSequence.proguardPhone() = "${this.substring(0, 3)}****${this.substring(7)}"

fun getIndexIndicator(index: Int, total: Int) = String.format("%d/%d", index, total)