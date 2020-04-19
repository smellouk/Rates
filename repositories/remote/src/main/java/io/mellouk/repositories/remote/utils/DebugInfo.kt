package io.mellouk.repositories.remote.utils

import okhttp3.Interceptor

class DebugInfo(val isDebug: Boolean, debugInterceptors: List<Interceptor>)