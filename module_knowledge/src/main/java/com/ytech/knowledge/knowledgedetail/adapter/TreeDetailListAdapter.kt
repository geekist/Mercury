package com.ytech.knowledge.knowledgedetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ytech.core.arouter.provider.WebViewWrapProvider
import com.ytech.model.DatasBean
import com.ytech.knowledge.R
import com.ytech.knowledge.databinding.TreeDetailListItemBinding


class TreeDetailListAdapter(context: Context) : PagedListAdapter<DatasBean, TreeDetailListAdapter.ViewHolder>(

    object : DiffUtil.ItemCallback<DatasBean>() {
            override fun areItemsTheSame(
                oldItem: DatasBean,
                newItem: DatasBean
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DatasBean,
                newItem: DatasBean
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    ) {

    private val mContext = context
    private val inflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<TreeDetailListItemBinding>(
            inflater,
            R.layout.tree_detail_list_item,
            parent,
            false
        )
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(getItem(position)!!)
    }

    inner class ViewHolder(
        itemView: View,
        binding: TreeDetailListItemBinding
    ) : RecyclerView.ViewHolder(itemView) {
        private val mBinding = binding
        fun setData(item: DatasBean) {
            if(item.desc.isNullOrEmpty()){
                item.desc = mContext.resources.getString(R.string.empty_description)
            }

            mBinding.item = item
            mBinding.itemParent.setOnClickListener {
                WebViewWrapProvider.instance.start(mContext, item.title, item.link)
            }
        }
    }
}