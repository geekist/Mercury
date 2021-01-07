package com.ytech.apply.apply.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ytech.apply.applydetail.ApplyItemFragment
import com.ytech.model.apply.ProjectTabItem

class ApplyFragmentStateAdapter(
    private val mData: MutableList<ProjectTabItem>,
    childFragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(childFragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        if (mData.size > 0) {
            return mData.size
        }
        return 0
    }

    override fun createFragment(position: Int): Fragment {
        val item = mData[position]
        return ApplyItemFragment.newInstance(item.id)
    }
}


