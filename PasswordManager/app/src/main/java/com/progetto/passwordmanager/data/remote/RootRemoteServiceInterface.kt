package com.progetto.passwordmanager.data.remote

import com.progetto.passwordmanager.data.remote.remotemodel.servicecredential.OldServiceCredentialRemoteModel
import com.progetto.passwordmanager.data.remote.remotemodel.servicecredential.ServiceCredentialRemoteModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RootRemoteServiceInterface {

    @GET("/")
    suspend fun getServiceCredential() : List<ServiceCredentialRemoteModel>

    @POST("/add/service")
    suspend fun addServiceCredential(@Body serviceCredential: ServiceCredentialRemoteModel): ApiResponse

    @POST("/mod/service")
    suspend fun modServiceCredential(@Body serviceCredential: OldServiceCredentialRemoteModel): ApiResponse

    @POST("/del/service")
    suspend fun delServiceCredential(@Body serviceCredential: ServiceCredentialRemoteModel): ApiResponse
}

data class ApiResponse (
    val message: String
)