package com.ytech.home.home

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import com.ytech.core.GlobalConfig
import com.ytech.core.net.model.NetResult
import com.ytech.home.home.model.Banner

import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {

    private val bannerLiveData = MutableLiveData<List<Banner>>()

    fun getBannerLiveData(): MutableLiveData<List<Banner>> {
        return bannerLiveData
    }

    fun getBanner() {
        viewModelScope.launch {
            when(val banner = HomeRepository.getBanner()) {
                is NetResult.Success ->bannerLiveData.postValue(banner.data)
                is NetResult.Error -> ToastUtils.show(banner.exception.msg)
            }
        }
    }
}