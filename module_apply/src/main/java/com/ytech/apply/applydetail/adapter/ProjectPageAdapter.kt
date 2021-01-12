package com.ytech.apply.applydetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ytech.apply.R
import com.ytech.ui.databinding.ImageListItemBinding
import com.ytech.core.GlobalConfig
import com.ytech.core.arouter.provider.WebViewWrapProvider
import com.ytech.model.apply.ProjectItemSub

class ProjectPageAdapter constructor(val context: Context) :
    PagedListAdapter<ProjectItemSub, ProjectPageAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<ProjectItemSub>() {
            override fun areItemsTheSame(
                oldItem: ProjectItemSub,
                newItem: ProjectItemSub
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProjectItemSub,
                newItem: ProjectItemSub
            ): Boolean {
                return oldItem == oldItem
            }
        }
    ) {

    private val mInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ImageListItemBinding.inflate(mInflater, parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.itemData = item
        holder.binding.context = context
        Glide.with(GlobalConfig.getApplicationContext())
            .asBitmap()
            .load(item!!.envelopePic)
                /*
            .listener(object : RequestListener<Bitmap?> {
                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    //TODO("Not yet implemented")
                    return true
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("Wain", "加载失败 errorMsg:" + e?.toString())
                    return false
                }
            })*/
            .apply(RequestOptions().placeholder(R.drawable.img_placeholder))
            .into(holder.binding.pic)

        holder.binding.root.setOnClickListener {
            WebViewWrapProvider.instance.start(context, item.title, item.link)
        }
    }

    class ViewHolder(
        itemView: View,
        val binding: ImageListItemBinding,
    ) : RecyclerView.ViewHolder(itemView)
}