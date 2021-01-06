package com.ytech.apply.apply

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import com.ytech.model.apply.ProjectTabItem
import com.ytech.core.net.model.NetResult

import kotlinx.coroutines.launch

class ProjectViewModel() : ViewModel() {

    private val tabDataLiveData = MutableLiveData<MutableList<com.ytech.model.apply.ProjectTabItem>>()

    fun getTabDataLiveData(): MutableLiveData<MutableList<com.ytech.model.apply.ProjectTabItem>> {
        return tabDataLiveData
    }

    fun getTabData(): MutableLiveData<MutableList<com.ytech.model.apply.ProjectTabItem>> {
        viewModelScope.launch {
            when(val tabData = ProjectRepository.getTabData()) {
                is NetResult.Success -> tabDataLiveData.postValue(tabData.data)
                is NetResult.Error -> ToastUtils.show(tabData.exception.msg)
            }
        }
        return tabDataLiveData
    }

}