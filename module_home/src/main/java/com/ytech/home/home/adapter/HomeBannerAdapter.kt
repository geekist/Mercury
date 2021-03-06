package com.ytech.home.home.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youth.banner.adapter.BannerAdapter
import com.ytech.core.GlobalConfig
import com.ytech.core.arouter.provider.WebViewWrapProvider
import com.ytech.model.home.Banner

class HomeBannerAdapter(dataList: List<Banner>)//设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
    : BannerAdapter<Banner, HomeBannerAdapter.BannerViewHolder>(dataList) {

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(holder: BannerViewHolder, data: Banner, position: Int, size: Int) {
        val context = GlobalConfig.getApplicationContext()
        Glide.with(context)
            .asBitmap()
            .load(data.imagePath)
            .into(holder.imageView)

        holder.imageView.setOnClickListener{
            WebViewWrapProvider.instance.start(context, data.title, data.url)
        }
    }

    inner class BannerViewHolder(var imageView: ImageView) :
        RecyclerView.ViewHolder(imageView)
}