package com.ytech.core.net.net

import android.util.Log
import com.ytech.core.net.exception.DealException
import com.ytech.core.net.exception.ResultException
import com.ytech.core.net.model.BaseModel
import com.ytech.core.net.model.NetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

open class BaseRepository {

    suspend fun <T:Any> requestResponse(
        api: suspend () -> BaseModel<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ) : NetResult<T> {
        return coroutineScope {
            try {
                val response = api()
                if (response.errorCode == -1) {
                    Log.d("mercury",response.errorMsg)
                    errorBlock?.let { it() }
                    NetResult.Error(
                        ResultException(response.errorCode.toString(), response.errorMsg)
                    )
                } else {
                    Log.d("mercury",response.data.toString())
                    successBlock?.let { it() }
                    NetResult.Success(response.data)
                }
            } catch (e: Exception) {
                Log.d("mercury",e.toString())
                //这里统一处理异常
                e.printStackTrace()
                NetResult.Error(DealException.handlerException(e))
            }
        }
    }






    suspend fun <T : Any> callRequest(
        call: suspend () -> NetResult<T>
    ): NetResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            //这里统一处理异常
            e.printStackTrace()
            NetResult.Error(DealException.handlerException(e))
        }
    }

    suspend fun <T : Any> handleResponse(
        response: BaseModel<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): NetResult<T> {
        return coroutineScope {
            if (response.errorCode == -1) {
                errorBlock?.let { it() }
                NetResult.Error(
                    ResultException(response.errorCode.toString(), response.errorMsg)
                )
            } else {
                successBlock?.let { it() }
                NetResult.Success(response.data)
            }
        }
    }
}