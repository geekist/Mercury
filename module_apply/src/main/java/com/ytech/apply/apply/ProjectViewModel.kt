package com.ytech.apply.apply

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import com.ytech.apply.apply.model.ProjectTabItem
import com.ytech.core.net.model.NetResult

import kotlinx.coroutines.launch

class ProjectViewModel() : ViewModel() {

    private val tabDataLiveData = MutableLiveData<MutableList<ProjectTabItem>>()

    fun getTabDataLiveData(): MutableLiveData<MutableList<ProjectTabItem>> {
        return tabDataLiveData
    }

    fun getTabData(): MutableLiveData<MutableList<ProjectTabItem>> {
        viewModelScope.launch {
            when(val tabData = ProjectRepository.getTabData()) {
                is NetResult.Success -> tabDataLiveData.postValue(tabData.data)
                is NetResult.Error -> ToastUtils.show(tabData.exception.msg)
            }
        }
        return tabDataLiveData
    }

}