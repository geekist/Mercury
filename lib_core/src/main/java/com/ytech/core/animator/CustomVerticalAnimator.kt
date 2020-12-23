package com.ytech.core.animator

import android.os.Parcelable
import com.ytech.core.R
import me.yokeyword.fragmentation.anim.FragmentAnimator

class CustomVerticalAnimator : FragmentAnimator(), Parcelable {
    init {
        enter = R.anim.v_fragment_enter_custom
        exit = R.anim.v_fragment_exit_custom
        popEnter = 0
        popExit = R.anim.v_fragment_pop_exit_custom
    }
}