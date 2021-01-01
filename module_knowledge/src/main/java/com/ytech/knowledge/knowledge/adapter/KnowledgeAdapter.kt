package com.ytech.knowledge.knowledge.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.ytech.common.common.buildBundle
import com.ytech.core.arouter.ARouterConstant
import com.ytech.core.arouter.ARouterUtils
import com.ytech.knowledge.R
import com.ytech.knowledge.databinding.TreeListItemBinding
import com.ytech.knowledge.knowledge.model.TreeData
import com.ytech.knowledge.knowledge.model.TreeDataItem
import com.ytech.ui.base.SupportFragment
import com.ytech.ui.widget.flowlayout.TagAdapter
import me.yokeyword.fragmentation.ISupportFragment

class KnowledgeAdapter(context: Context, data: MutableList<TreeData>) :
    RecyclerView.Adapter<KnowledgeAdapter.ViewHolder>() {

    private val mContext = context
    private var mData: MutableList<TreeData> = data

    private var mOnItemClickListener: ((treeData: TreeDataItem, position: Int) -> Unit)? = null
    fun mOnItemClickListener(l: (treeData:TreeDataItem,position: Int) -> Unit) {
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
            treeDataItem: TreeData,
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