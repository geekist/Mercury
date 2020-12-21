package com.ytech.common.common

//import me.yokeyword.fragmentation.ISupportFragment.RESULT_OK
import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hjq.toast.ToastUtils
import com.lxj.xpopup.XPopup
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.yanzhenjie.permission.AndPermission
import com.ytech.common.device.dp2px
import com.ytech.common.text.isEmptyStr
import java.security.MessageDigest
import java.util.*
import kotlin.math.*

fun isUIThread() = Looper.myLooper() == Looper.getMainLooper()


/**
 * 查找元素并返回元素对应index
 */
fun <T> searchReturnIndex(list: MutableList<T>, predicate: (element: T) -> Boolean): Int {
    val index = 0
    list.forEachIndexed { i, element ->
        if (predicate(element)) return i
    }
    return index
}

fun <T> searchReturnIndexAndElement(
    list: Collection<T>, predicate: (element: T) -> Boolean,
    indexAndElement: (index: Int, element: T) -> Unit
) {
    for ((i, element) in list.withIndex()) {
        if (predicate(element)) {
            indexAndElement(i, element)
            break
        }
    }
}


fun TextView.drawable(
    @DrawableRes drawableLeftResId: Int? = null,
    @DrawableRes drawableTopResId: Int? = null,
    @DrawableRes drawableRightResId: Int? = null,
    @DrawableRes drawableBottomResId: Int? = null,
    drawablePadding: Int? = null
) {
    val drawableLeft =
        if (drawableLeftResId != null) context.loadDrawable(drawableLeftResId) else null
    val drawableTop = if (drawableTopResId != null) context.loadDrawable(drawableTopResId) else null
    val drawableRight =
        if (drawableRightResId != null) context.loadDrawable(drawableRightResId) else null
    val drawableBottom =
        if (drawableBottomResId != null) context.loadDrawable(drawableBottomResId) else null
    drawablePadding?.let { compoundDrawablePadding = dp2px(context, it.toFloat()) }
    setCompoundDrawablesWithIntrinsicBounds(
        drawableLeft,
        drawableTop,
        drawableRight,
        drawableBottom
    )
}

fun EditText.changeTextSizeWhenTextChange(emptyTextSize: Float, notEmptyTextSize: Float) {
    textSize = if (text.isEmptyStr()) emptyTextSize else notEmptyTextSize
}


fun Int.obtainFragmentResult(
    requestCode: Int, resultCode: Int, data: Bundle?,
    result: (result: Bundle) -> Unit
) {
    if (this == requestCode && resultCode == RESULT_OK && data != null) {
        result(data)
    }
}

fun Int.fragmentResultOk(requestCode: Int, resultCode: Int, operate: () -> Unit) {
    if (this == requestCode && resultCode == RESULT_OK) {
        operate()
    }
}

fun RecyclerView.clearRvAnim() {
    val animator = itemAnimator
    if (animator != null && animator is DefaultItemAnimator)
        animator.supportsChangeAnimations = false
    itemAnimator?.changeDuration = 400
    itemAnimator?.moveDuration = 400
}

fun RecyclerView.clearRvFocus() {
    isFocusable = false
    isFocusableInTouchMode = false
    isNestedScrollingEnabled = false
}

fun NestedScrollView.clearScrollViewFocus() {
    isFocusable = false
    isFocusableInTouchMode = false
    isNestedScrollingEnabled = false
}



fun <T, R> T?.nullJudge(notNull: (obj: T) -> R, isNull: () -> R) = if (this != null) {
    notNull(this)
} else {
    isNull()
}

fun String.md5(): String {
    val digest = MessageDigest.getInstance("MD5")
    val result = digest.digest(toByteArray())
    return with(StringBuilder()) {
        result.forEach {
            val hex = it.toInt() and (0xFF)
            val hexStr = Integer.toHexString(hex)
            if (hexStr.length == 1) {
                this.append("0").append(hexStr)
            } else {
                this.append(hexStr)
            }
        }
        this.toString()
    }
}

fun fastShow(text: CharSequence) {
    ToastUtils.getToast().setText(text)
    ToastUtils.getToast().show()
}

fun fastShow(resId: Int) {
    ToastUtils.getToast().setText(resId)
    ToastUtils.getToast().show()
}

fun Int.isValidPwdLength() = this >= Constants.MIN_PWD_LENGTH && this <= Constants.MAX_PWD_LENGTH

fun TextView.isValidPwd(password: CharSequence?) {
    val length = password?.length ?: 0
    isEnabled = length.isValidPwdLength()
}

fun <T> Collection<T>?.isNotEmptyList(block: ((list: Collection<T>) -> Unit)? = null) {
    if (!this.isNullOrEmpty()) {
        block?.invoke(this)
    }
}

fun <T> Collection<T>?.isEmptyList(block: (() -> Unit)? = null) {
    if (this.isNullOrEmpty()) {
        block?.invoke()
    }
}

fun <T> SmartRefreshLayout.finishLoadMoreWithNoMoreData(
    loadList: Collection<T>?, isRefresh: Boolean
) {
    if (!isRefresh && loadList.isNullOrEmpty()) finishLoadMoreWithNoMoreData()
}

fun BaseQuickAdapter<*, *>.finishLoadMoreWithNoMoreData(
    loadList: Collection<*>?, isRefresh: Boolean
) {
    if (!isRefresh && loadList.isNullOrEmpty()) loadMoreEnd()
}

fun AppCompatEditText.searchAction(
    hint: String = "请输入搜索关键字",
    ignoreEmptyStr: Boolean = false,
    block: (searchText: String) -> Unit
) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val searchText = text.toString()
            if (searchText.isEmptyStr() && !ignoreEmptyStr) {
                ToastUtils.show(hint)
            } else {
                block(searchText)
            }
            true
        } else {
            false
        }
    }
}

fun AppCompatEditText.sendAction(
    hint: String = "请输入内容", clearContent: Boolean = false,
    block: (messageContent: String) -> Unit
) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            val messageContent = text.toString()
            if (messageContent.isEmptyStr()) {
                ToastUtils.show(hint)
            } else {
                if (clearContent) setText("")
                block(messageContent)
            }
            true
        } else {
            false
        }
    }
}

fun getDistance(
    longitude1: Double, latitude1: Double,
    longitude2: Double, latitude2: Double
): Double {
    val lat1 = rad(latitude1)
    val lat2 = rad(latitude2)
    val a = lat1 - lat2
    val b = rad(longitude1) - rad(longitude2)
    var s = 2 * asin(
        sqrt(
            sin(a / 2).pow(2)
                    + cos(lat1) * cos(lat1)
                    * sin(b / 2).pow(2)
        )
    )
    s *= 6378137.0
    s = ((s * 10000).roundToInt() / 10000).toDouble()
    return s
}

private fun rad(d: Double): Double {
    return d * Math.PI / 180.0
}

/**
 * xx->xx
 * xxx->xx
 * xxxx->xx
 */
fun String.getSimpleName() = when (length) {
    0 -> ""
    1 -> this
    2 -> this
    3 -> substring(1, length)
    4 -> substring(2, length)
    else -> this
}

fun RecyclerView._smoothScrollToPosition(position: Int) {
    val layoutManager = layoutManager ?: return
    if (layoutManager is LinearLayoutManager) {
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        when {
            position in firstVisibleItemPosition..lastVisibleItemPosition -> {
                smoothScrollToPosition(position)
            }
            position < firstVisibleItemPosition && firstVisibleItemPosition - position > 10 -> {
                scrollToPosition(
                    position +
                            10
                )
                smoothScrollToPosition(position)
            }
            position < firstVisibleItemPosition && firstVisibleItemPosition - position <= 10 -> {
                smoothScrollToPosition(position)
            }
            position > lastVisibleItemPosition && position - lastVisibleItemPosition > 10 -> {
                scrollToPosition(position - 10)
                smoothScrollToPosition(position)
            }
            position > lastVisibleItemPosition && position - lastVisibleItemPosition <= 10 -> {
                smoothScrollToPosition(position)
            }
            else -> smoothScrollToPosition(position)
        }
    }
}

fun Long.formatMessageTime(): String {
    val weekNames = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
    val hourTimeFormat = "HH:mm"
    val monthTimeFormat = "M月d日 HH:mm"
    val yearTimeFormat = "yyyy年M月d日 HH:mm"
    try {
        val todayCalendar = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this

        if (todayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            // 当年
            if (todayCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                // 当月
                val temp =
                    todayCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH)
                return when (temp) {
                    0 -> this.format2DateString(hourTimeFormat)
                    1 -> "昨天 ${this.format2DateString(hourTimeFormat)}"
                    2, 3, 4, 5, 6 -> {
                        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                        return "${weekNames[dayOfWeek - 1]} ${this.format2DateString(hourTimeFormat)}"
                    }
                    else -> this.format2DateString(hourTimeFormat)
                }
            } else {
                return this.format2DateString(monthTimeFormat)
            }
        } else {
            return this.format2DateString(yearTimeFormat)
        }
    } catch (e: Exception) {
        return ""
    }
}


fun getChatPopupBubbleDialogOffsetX(bubbleDialogWidth: Int, destWidth: Int): Int {
    return ((destWidth - bubbleDialogWidth) * 0.5f).toInt()
}

fun toClipboard(context: Context, label: CharSequence, content: CharSequence) {
    val clipboardManager = context.clipboardManager
    val clip = ClipData.newPlainText(label, content)
    clipboardManager?.setPrimaryClip(clip)
}

fun IntArray.getOptions() = map { element ->
    when (element) {
        0 -> "A"
        1 -> "B"
        2 -> "C"
        3 -> "D"
        4 -> "E"
        5 -> "F"
        6 -> "G"
        7 -> "H"
        8 -> "I"
        9 -> "J"
        else -> "A"
    }
}
