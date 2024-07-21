package com.progetto.passwordmanager.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object ServiceCredentialApi {

    //private const val BASE_URL = "http://192.168.45.238:3000"
    private const val BASE_URL = "http://192.168.50.96:3000"

    private val moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit
        .Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    val rootService: RootRemoteServiceInterface by lazy { retrofit.create(RootRemoteServiceInterface::class.java) }
}