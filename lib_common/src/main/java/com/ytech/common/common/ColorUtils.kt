package com.ytech.common.common

import android.graphics.Color
import androidx.annotation.ColorInt

object ColorUtils {
    fun stripAlpha(@ColorInt color: Int): Int {
        return -16777216 or color
    }

    @ColorInt
    fun shiftColor(@ColorInt color: Int, /*@FloatRange(from = 0.0, to = 2.0)*/ by: Float): Int {
        return if (by == 1.0f) {
            color
        } else {
            val alpha = Color.alpha(color)
            val hsv = FloatArray(3)
            Color.colorToHSV(color, hsv)
            hsv[2] *= by
            (alpha shl 24) + (16777215 and Color.HSVToColor(hsv))
        }
    }

    @ColorInt
    fun darkenColor(@ColorInt color: Int): Int {
        return shiftColor(color, 0.9f)
    }

    @ColorInt
    fun lightenColor(@ColorInt color: Int): Int {
        return shiftColor(color, 1.1f)
    }

    fun isColorLight(@ColorInt color: Int): Boolean {
        val darkness =
            1.0 - (0.299 * Color.red(color).toDouble() + 0.587 * Color.green(color).toDouble() + 0.114 * Color.blue(color).toDouble()) / 255.0
        return darkness < 0.4
    }

    @ColorInt
    fun invertColor(@ColorInt color: Int): Int {
        val r = 255 - Color.red(color)
        val g = 255 - Color.green(color)
        val b = 255 - Color.blue(color)
        return Color.argb(Color.alpha(color), r, g, b)
    }

    @ColorInt
    fun adjustAlpha(@ColorInt color: Int, /*@FloatRange(from = 0.0, to = 1.0)*/ factor: Float): Int {
        val alpha = Math.round(Color.alpha(color).toFloat() * factor)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    @ColorInt
    fun withAlpha(@ColorInt baseColor: Int, /*@FloatRange(from = 0.0, to = 1.0)*/ alpha: Float): Int {
        val a = Math.min(255, Math.max(0, (alpha * 255.0f).toInt())) shl 24
        val rgb = 16777215 and baseColor
        return a + rgb
    }

    fun blendColors(color1: Int, color2: Int, /*@FloatRange(from = 0.0, to = 1.0)*/ ratio: Float): Int {
        val inverseRatio = 1.0f - ratio
        val a = Color.alpha(color1).toFloat() * inverseRatio + Color.alpha(color2).toFloat() * ratio
        val r = Color.red(color1).toFloat() * inverseRatio + Color.red(color2).toFloat() * ratio
        val g = Color.green(color1).toFloat() * inverseRatio + Color.green(color2).toFloat() * ratio
        val b = Color.blue(color1).toFloat() * inverseRatio + Color.blue(color2).toFloat() * ratio
        return Color.argb(a.toInt(), r.toInt(), g.toInt(), b.toInt())
    }

    /**
     * color转RGB
     */
    fun colorToRGB(color: Int): IntArray {
        val rgb = IntArray(3)
        rgb[0] = (color and 0xff0000) shr 16
        rgb[1] = (color and 0x00ff00) shr 8
        rgb[2] = color and 0x0000ff
        return rgb
    }

    /**
     * RGB转color
     */
    fun rgbToColor(r: Int, g: Int, b: Int): Int {
        var r = r
        var g = g
        var b = b
        r = judgeRGB(r)
        g = judgeRGB(g)
        b = judgeRGB(b)
        return Color.rgb(r, g, b)
    }

    private fun judgeRGB(colorValue: Int): Int {
        var colorValue = colorValue
        if (colorValue > 255) colorValue = 255
        if (colorValue < 0) colorValue = 0
        return colorValue
    }
}