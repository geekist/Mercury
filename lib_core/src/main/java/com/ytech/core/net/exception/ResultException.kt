package com.ytech.core.net.exception


class ResultException(var errCode: String?, var msg: String?) : Exception(msg)
