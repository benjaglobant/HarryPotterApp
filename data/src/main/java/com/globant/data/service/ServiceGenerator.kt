package com.globant.data.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {
    private val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val defaultRequest = chain.request()

        val defaultHttpUrl = defaultRequest.url()
        val httpUrl = defaultHttpUrl.newBuilder()
            .addQueryParameter(KEY, KEY_VALUE)
            .build()

        val requestBuilder = defaultRequest.newBuilder().url(httpUrl)

        chain.proceed(requestBuilder.build())
    }

    private val builder = Retrofit.Builder()
        .baseUrl(HARRY_POTTER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    fun <S> createService(serviceClass: Class<S>): S {
        val retrofit = builder.client(httpClient.build()).build()
        return retrofit.create(serviceClass)
    }

    companion object {
        private const val HARRY_POTTER_BASE_URL = "https://www.potterapi.com"
        private const val KEY = "key"
        private const val KEY_VALUE = "$2a$10\$Vd/eRtaSg4bxUNo6jYGkMuUTdK9UZMorHxbjz8Mc/9oA7AscHWDy6"
    }
}
