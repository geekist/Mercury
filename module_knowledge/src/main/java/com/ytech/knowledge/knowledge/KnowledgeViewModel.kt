package com.ytech.knowledge.knowledge

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ytech.core.GlobalConfig
import com.ytech.core.net.model.NetResult
import com.ytech.model.knowledge.TreeData
import kotlinx.coroutines.launch


class KnowledgeViewModel() : ViewModel() {

    private val knowledgeLiveData = MutableLiveData<MutableList<com.ytech.model.knowledge.TreeData>>()

    fun getKnowledgeLiveData(): MutableLiveData<MutableList<com.ytech.model.knowledge.TreeData>> {
        return knowledgeLiveData
    }

    fun getTreeList() {
        viewModelScope.launch {
            when(val treeData = KnowledgeRepository.getTreeList()) {
                is NetResult.Success -> knowledgeLiveData.postValue(treeData.data)
                is NetResult.Error -> Toast.makeText(
                    GlobalConfig.getApplicationContext(),
                    treeData.exception.msg,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}