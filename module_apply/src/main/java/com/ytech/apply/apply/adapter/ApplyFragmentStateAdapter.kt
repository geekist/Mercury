package com.ytech.apply.apply.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ytech.apply.apply.model.ProjectTabItem
import com.ytech.apply.applydetail.ApplyItemFragment

class ApplyFragmentStateAdapter(
    private val mData: MutableList<ProjectTabItem>,
    childFragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(childFragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        if (mData != null && mData!!.size > 0) {
            return mData!!.size
        }
        return 0
    }

    override fun createFragment(position: Int): Fragment {
        val item = mData!![position]
        return createItemFragment(item.id)
    }

    private fun createItemFragment(id: Int): Fragment {
        return ApplyItemFragment.newInstance(id)
    }
}


