package com.ytech.home.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import com.ytech.core.net.model.NetResult
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {

    private val bannerLiveData = MutableLiveData<List<com.ytech.model.home.Banner>>()

    fun getBannerLiveData(): MutableLiveData<List<com.ytech.model.home.Banner>> {
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