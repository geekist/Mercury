package com.ytech.home.homelist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ytech.core.arouter.provider.WebViewWrapProvider
import com.ytech.home.R
import com.ytech.ui.databinding.ListItem1Binding

import com.ytech.model.DatasBean

class HomeListAdapter(context: Context) :
    PagedListAdapter<DatasBean, HomeListAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<DatasBean>() {
        override fun areContentsTheSame(
            oldItem: DatasBean,
            newItem: DatasBean
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem: DatasBean,
            newItem: DatasBean
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }) {


    private val inflater = LayoutInflater.from(context)
    private val mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItem1Binding.inflate(inflater, parent, false)
        return ViewHolder(binding.root, binding, mContext)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(getItem(position)!!)
    }


    class ViewHolder(
        itemView: View,
        binding: ListItem1Binding,
        context: Context
    ) : RecyclerView.ViewHolder(itemView) {

        private var mBinding = binding
        private var mContext = context

        fun setData(datasBean: DatasBean) {
           if(datasBean.desc.isNullOrEmpty()){
               datasBean.desc = mContext.resources.getString(R.string.empty_description)
           }
            mBinding.feed = datasBean

            mBinding.parentItem.setOnClickListener {
                WebViewWrapProvider.instance.start(mContext, datasBean.title, datasBean.link)
            }
        }

    }
}