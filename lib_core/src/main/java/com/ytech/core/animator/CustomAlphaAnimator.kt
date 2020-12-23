package com.ytech.core.animator

import android.os.Parcelable
import com.ytech.core.R
import me.yokeyword.fragmentation.anim.FragmentAnimator

class CustomAlphaAnimator : FragmentAnimator(), Parcelable {
    init {
        enter = R.anim.a_fragment_enter_custom
        exit = R.anim.a_fragment_exit_custom
        popEnter = 0
        popExit = 0
    }
}