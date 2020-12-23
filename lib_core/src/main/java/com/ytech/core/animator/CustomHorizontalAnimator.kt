package com.ytech.core.animator

import android.os.Parcelable
import com.ytech.core.R
import me.yokeyword.fragmentation.anim.FragmentAnimator

class CustomHorizontalAnimator : FragmentAnimator(), Parcelable {
    init {
        enter = R.anim.h_fragment_enter_custom
        exit = R.anim.h_fragment_exit_custom
        popEnter = 0
        popExit = R.anim.h_fragment_pop_exit_custom
    }
}