package com.progetto.passwordmanager.data.remote.remotemodel.servicecredential

data class ServiceCredentialRemoteModel(
    val Service: String,
    val User: String,
    val Password: String
)

data class OldServiceCredentialRemoteModel(
    val Service: String,
    val User: String,
    val Password: String,
    val OldService: String,
    val OldUser: String,
    val OldPassword: String
)