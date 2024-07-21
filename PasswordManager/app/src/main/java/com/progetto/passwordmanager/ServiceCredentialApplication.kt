package com.progetto.passwordmanager

import android.app.Application
import com.progetto.passwordmanager.data.ServiceCredentialRepository
import com.progetto.passwordmanager.data.local.ServiceCredentialRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ServiceCredentialApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { ServiceCredentialRoomDatabase
                                        .getDatabase(this, applicationScope) }
    val repository by lazy { ServiceCredentialRepository(database.serviceCredentialDao()) }
}