package com.ytech.knowledge.knowledge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ytech.knowledge.R
import com.ytech.knowledge.databinding.TreeListItemBinding
import com.ytech.model.knowledge.TreeData
import com.ytech.model.knowledge.TreeDataItem
import com.ytech.ui.widget.flowlayout.TagAdapter

class KnowledgeAdapter(context: Context, data: MutableList<com.ytech.model.knowledge.TreeData>) :
    RecyclerView.Adapter<KnowledgeAdapter.ViewHolder>() {

    private val mContext = context
    private var mData: MutableList<com.ytech.model.knowledge.TreeData> = data

    private var mOnItemClickListener: ((treeData: com.ytech.model.knowledge.TreeDataItem, position: Int) -> Unit)? = null
    fun mOnItemClickListener(l: (treeData: com.ytech.model.knowledge.TreeDataItem, position: Int) -> Unit) {
        this.mOnItemClickListener = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<TreeListItemBinding>(
            LayoutInflater.from(mContext),
            R.layout.tree_list_item,
            parent,
            false
        )
        return ViewHolder(binding.root, binding)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mData[position], mContext)
    }

    inner class ViewHolder(
        itemView: View,
        binding: TreeListItemBinding
    ) : RecyclerView.ViewHolder(itemView) {

        private val mBinding = binding

        fun setData(
            treeDataItem: com.ytech.model.knowledge.TreeData,
            mContext: Context
        ) {

            mBinding.treeData = treeDataItem

            val tagChildren = treeDataItem.children

            mBinding.mFlowLayout.setAdapter(object : TagAdapter() {
                override fun getItemCount(): Int {
                    return tagChildren.size
                }

                override fun createView(
                    inflater: LayoutInflater,
                    parent: ViewGroup,
                    position: Int
                ): View {
                    return inflater.inflate(R.layout.flow_layout_item, parent, false)
                }

                override fun bindView(view: View, position: Int) {

                    (view as TextView).text = tagChildren[position].name

                }

                override fun onItemViewClick(view: View, position: Int) {
                    val item = tagChildren[position]
                    this@KnowledgeAdapter.mOnItemClickListener?.invoke(item,position)


                }
            })
        }

    }
}