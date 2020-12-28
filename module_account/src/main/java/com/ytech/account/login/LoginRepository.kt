package com.ytech.account.login

import com.ytech.account.api.AccountApiService
import com.ytech.core.model.User
import com.ytech.core.net.model.BaseModel
import com.ytech.core.net.model.NetResult
import com.ytech.core.net.net.BaseRepository
import com.ytech.core.net.net.RetrofitClient

object LoginRepository : BaseRepository() {

    suspend fun login(username: String, password: String): NetResult<User> {
        return requestResponse(api = { RetrofitClient.create<AccountApiService>().login(username, password)})
    }

    suspend fun register(
        username: String,
        password: String,
        surePassword: String
    ): NetResult<User> {
        return requestResponse(api = {RetrofitClient.create<AccountApiService>().register(username, password, surePassword)})
    }
}