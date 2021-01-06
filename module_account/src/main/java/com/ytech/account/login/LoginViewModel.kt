package com.ytech.account.login

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ytech.core.GlobalConfig
import com.ytech.model.BaseContext
import com.ytech.model.User
import com.ytech.core.net.model.NetResult
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {

    private val loginLiveData = MutableLiveData<com.ytech.model.User>()

    fun login(username: String, password: String): MutableLiveData<com.ytech.model.User> {
        viewModelScope.launch {
            when(val user = LoginRepository.login(username, password)) {
                is NetResult.Success -> loginLiveData.postValue(user.data)
                is NetResult.Error -> Toast.makeText(
                    GlobalConfig.getApplicationContext(),
                    user.exception.msg,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return loginLiveData
    }

    fun register(username: String, password: String, surePassword: String): MutableLiveData<com.ytech.model.User> {
        viewModelScope.launch {
            val user = LoginRepository.register(username, password, surePassword)
            if (user is NetResult.Success) {
                loginLiveData.postValue(user.data)
            } else if (user is NetResult.Error) {
                Toast.makeText(
                    com.ytech.model.BaseContext.instance.getContext(),
                    user.exception.msg,
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        return loginLiveData
    }

    class LoginViewModelFactory(private val loginRepository : LoginRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel() as T
        }
    }

}