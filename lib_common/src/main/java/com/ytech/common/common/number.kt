package com.ytech.common.common

import android.graphics.Color
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import java.text.DecimalFormat

/**
 * 限制小数点只能输入两位
 */
fun EditText.limitDecimalPoint() {
    val editStr = editableText.toString().trim()
    val posDot = editStr.indexOf(".")
    // 不允许输入3位小数,超过三位就删掉
    if (posDot < 0) {
        return
    }
    if (editStr.length - posDot - 1 > 2) {
        editableText?.delete(posDot + 3, posDot + 4)
    }
}

/**
 * 183 5715 5795 -> 18357155795
 */
fun EditText.getPhoneNumber() = text.toString().trim().replace(" ", "")

fun EditText.clearSpace() = text.toString().trim().replace(" ", "")

fun CharSequence?.getMoney(): Double {
    if (isNullOrEmpty()) return 0.0
    return try {
        toString().toDouble()
    } catch (e: Exception) {
        0.0
    }
}

/**
 * 隐私显示手机号
 * 如：130****0000
 */
fun CharSequence?.proguardPhone(): String {
    if (this.isNullOrEmpty() || this.length != 11) {
        return ""
    }
    return this.substring(0, 3) + "****" + this.substring(7)
}

fun Double.formatMoney(): String = DecimalFormat("###,##0.00").format(this)

// 有效期 MM/YY
fun CharSequence?.formatValidPeriod(): String {
    if (this.isNullOrEmpty()) return ""
    when (this.length) {
        2 -> {

        }
    }
    return ""
}

/**
 * @param flag true表示密码输入为密文 否则输入为明文
 */
fun EditText.switchPwdInputType(flag: Boolean) {
    transformationMethod = if (flag)
        PasswordTransformationMethod.getInstance()
    else
        HideReturnsTransformationMethod.getInstance()
    setSelection(length())
}

// 银行卡类型:1.储蓄卡，2.信用卡
fun Int.getBankCardType() = when (this) {
    2 -> "信用卡"
    1 -> "储蓄卡"
    else -> "未知"
}

// 6225 **** **** 8868
fun Long.formatBankCardNumber(): String {
    val cardNumberStr = this.toString()
    if (cardNumberStr.length >= 8) {
        return "${cardNumberStr.substring(0, 4)} **** **** ${cardNumberStr.substring(
            cardNumberStr.length - 4, cardNumberStr.length
        )}"
    }
    return ""
}

// 银行卡尾号
fun Long.getBankCardTailNumber(): String {
    val cardNumberStr = this.toString()
    if (cardNumberStr.length >= 4) {
        return cardNumberStr.substring(cardNumberStr.length - 4, cardNumberStr.length)
    }
    return ""
}

// 对应logo的主色调:1 红，2：绿，3：蓝，4：黄，5：桔
fun Int.getBankCardBgGradientColor() = when (this) {
    1 -> Color.parseColor("#24dd444f")
    2 -> Color.parseColor("#24388eb2")
    3 -> Color.parseColor("#243688d9")
    4 -> Color.parseColor("#24cfa862")
    5 -> Color.parseColor("#24fb9232")
    else -> Color.TRANSPARENT
}

fun Int.getBankCardShadowColor() = when (this) {
    1 -> Color.parseColor("#82dd444f")
    2 -> Color.parseColor("#82388eb2")
    3 -> Color.parseColor("#823688d9")
    4 -> Color.parseColor("#82cfa862")
    5 -> Color.parseColor("#82fb9232")
    else -> Color.parseColor("#33000000")
}

fun Int.getBankCardStartColor() = when (this) {
    1 -> Color.parseColor("#dc444f")
    2 -> Color.parseColor("#388eb2")
    3 -> Color.parseColor("#3688d9")
    4 -> Color.parseColor("#cfa762")
    5 -> Color.parseColor("#fb9232")
    else -> Color.parseColor("#fb9232")
}

fun Int.getBankCardEndColor() = when (this) {
    1 -> Color.parseColor("#df5a4e")
    2 -> Color.parseColor("#44ac9f")
    3 -> Color.parseColor("#699eeb")
    4 -> Color.parseColor("#dec76e")
    5 -> Color.parseColor("#ffaf5f")
    else -> Color.parseColor("#ffaf5f")
}
