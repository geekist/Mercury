package com.ytech.account.login

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ytech.core.GlobalConfig
import com.ytech.core.model.BaseContext
import com.ytech.core.model.User
import com.ytech.core.net.model.NetResult
import kotlinx.coroutines.launch

/**
 * Create by liwen on 2020/5/27
 */
class LoginViewModel() : ViewModel() {

    private val loginLiveData = MutableLiveData<User>()

    fun login(username: String, password: String): MutableLiveData<User> {
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

    fun register(username: String, password: String, surePassword: String): MutableLiveData<User> {
        viewModelScope.launch {
            val user = LoginRepository.register(username, password, surePassword)
            if (user is NetResult.Success) {
                loginLiveData.postValue(user.data)
            } else if (user is NetResult.Error) {
                Toast.makeText(
                    BaseContext.instance.getContext(),
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