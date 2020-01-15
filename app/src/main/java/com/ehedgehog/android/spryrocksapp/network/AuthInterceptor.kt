package com.ehedgehog.android.spryrocksapp.network

import com.ehedgehog.android.spryrocksapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter("key", BuildConfig.API_KEY)
            .addQueryParameter("token", BuildConfig.TOKEN)
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }

}