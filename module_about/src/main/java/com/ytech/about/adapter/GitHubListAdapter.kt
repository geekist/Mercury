package com.ytech.about.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hjq.toast.ToastUtils
import com.ytech.about.R
import com.ytech.core.GlobalConfig
import com.ytech.core.arouter.provider.WebViewWrapProvider
import com.ytech.model.about.GitItem

class GitHubListAdapter (val objectItemList: List<GitItem>) :
    RecyclerView.Adapter<GitHubListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val objName: TextView = view.findViewById(R.id.gitName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.git_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GitHubListAdapter.ViewHolder, position: Int) {
        val objItem = objectItemList[position]
        holder.objName.text = objItem.name
        holder.itemView.setOnClickListener{
            WebViewWrapProvider.instance.start(GlobalConfig.getApplicationContext(), objItem.name,objItem.url)
        }
    }

    override fun getItemCount(): Int {
        return objectItemList.size
    }
}