package com.ytech.knowledge.knowledge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import com.ytech.core.net.model.NetResult
import com.ytech.model.knowledge.TreeData
import kotlinx.coroutines.launch


class KnowledgeViewModel() : ViewModel() {

    private val knowledgeLiveData = MutableLiveData<MutableList<TreeData>>()

    fun getKnowledgeLiveData(): MutableLiveData<MutableList<TreeData>> {
        return knowledgeLiveData
    }

    fun getTreeList() {
        viewModelScope.launch {
            when(val treeData = KnowledgeRepository.getTreeList()) {
                is NetResult.Success -> knowledgeLiveData.postValue(treeData.data)
                is NetResult.Error -> ToastUtils.show(treeData.exception.msg)
            }
        }
    }
}