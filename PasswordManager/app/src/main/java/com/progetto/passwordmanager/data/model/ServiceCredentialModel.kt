package com.progetto.passwordmanager.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceCredentialModel(
    val service: String,
    val user: String,
    val password: String
) : Parcelable
