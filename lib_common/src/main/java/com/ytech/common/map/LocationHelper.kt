package com.ytech.common.map

import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

class LocationHelper : AMapLocationListener {
    private val mLocationClientOption by lazy {
        val locationClientOption = AMapLocationClientOption()
        locationClientOption.interval = 2000
        locationClientOption.isOnceLocation = false
        // 设置为高精度定位模式
        locationClientOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        locationClientOption
    }
    private var mLocationClient: AMapLocationClient? = null

    private var mOnLocationSuccess: ((location: AMapLocation) -> Unit)? = null
    private var mOnLocationFailed: (() -> Unit)? = null

    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    /**
     * 开始定位
     */
    fun startLocation(
        context: Context, onStart: (() -> Unit)? = null,
        onLocationSuccess: (location: AMapLocation) -> Unit, onLocationFailed: () -> Unit
    ) {
        onStart?.invoke()
        this.mOnLocationSuccess = onLocationSuccess
        this.mOnLocationFailed = onLocationFailed

        if (mLocationClient == null) mLocationClient = AMapLocationClient(context)
        //设置定位监听
        mLocationClient?.setLocationListener(this)
        //设置定位参数
        mLocationClient?.setLocationOption(mLocationClientOption)
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mLocationClient?.startLocation()
    }

    fun stopLocation() {
        mLocationClient?.stopLocation()
        mLocationClient = null
    }

    override fun onLocationChanged(location: AMapLocation?) {
        if (location != null && location.errorCode == 0) {
            // 定位成功回调信息，设置相关消息
            mOnLocationSuccess?.invoke(location)
        } else {
            // 定位失败
            mOnLocationFailed?.invoke()
        }
    }

    private object Holder {
        val INSTANCE = LocationHelper()
    }
}