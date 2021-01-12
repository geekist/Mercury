package com.ytech.ui.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.SectionEntity

abstract class BaseSectionAdapter<E : Any, T : SectionEntity<E>, K : BaseViewHolder>(
    layoutResId: Int, data: MutableList<T>?, private val sectionHeaderResId: Int
) : BaseSelectAdapter<T, K>(layoutResId, data) {
    companion object {
        private const val VIEW_TYPE_SECTION_HEADER = 0x00000444
    }

    override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): K {
        if (viewType == VIEW_TYPE_SECTION_HEADER)
            return createBaseViewHolder(getItemView(sectionHeaderResId, parent))
        return super.onCreateDefViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: K, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_SECTION_HEADER) {
            val item = getItem(position) ?: return
            convertSectionHeader(holder, item, position)
        } else {
            super.onBindViewHolder(holder, position)
        }
    }

    protected abstract fun convertSectionHeader(holder: K, sectionItem: T, position: Int)

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (item != null && item.isHeader)
            return VIEW_TYPE_SECTION_HEADER
        return super.getItemViewType(position)
    }
}