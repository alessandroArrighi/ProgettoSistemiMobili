package com.progetto.passwordmanager.data

import android.util.Log
import androidx.annotation.WorkerThread
import com.progetto.passwordmanager.data.local.ServiceCredential
import com.progetto.passwordmanager.data.local.ServiceCredentialDao
import com.progetto.passwordmanager.data.model.ServiceCredentialModel
import com.progetto.passwordmanager.data.remote.ServiceCredentialApi
import com.progetto.passwordmanager.data.remote.remotemodel.servicecredential.OldServiceCredentialRemoteModel
import com.progetto.passwordmanager.data.remote.remotemodel.servicecredential.ServiceCredentialRemoteModel
import kotlin.Exception

class ServiceCredentialRepository (
    private val serviceCredentialLocal: ServiceCredentialDao
) {

    private val TAG = "Connection Error"

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertLocalServiceCredential(serviceCredentialModel: ServiceCredentialModel) {
        serviceCredentialLocal.insertUtenza(
            this.toServiceCredential(serviceCredentialModel)
        )
    }

    suspend fun insertRemoteServiceCredential(serviceCredentialModel: ServiceCredentialModel) {
        try {
            ServiceCredentialApi.rootService.addServiceCredential(
                this.toServiceCredentialRemoteModel(serviceCredentialModel)
            )
        } catch (error: Exception) {
            Log.e(TAG, error.toString())
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAllLocalServiceCredential() : List<ServiceCredentialModel> {
        return serviceCredentialLocal.getUtenze().map {
            ServiceCredentialModel(
                service = it.Service,
                user = it.User,
                password = it.Password
            )
        }
    }

    suspend fun updateServiceCredentialRemote() {
        try {
            val allServiceCredentialRemote = ServiceCredentialApi
                .rootService
                .getServiceCredential()

            val allServiceCredentialLocal = serviceCredentialLocal
                .getUtenze()
                .map {
                    ServiceCredentialRemoteModel(
                        Service = it.Service,
                        User = it.User,
                        Password = it.Password
                    )
                }

            // aggiungi i servizi che non sono salvati lato server
            var updateList = allServiceCredentialLocal.subtract(allServiceCredentialRemote)

            if(updateList.isNotEmpty()) {
                updateList.forEach {
                    ServiceCredentialApi.rootService.addServiceCredential(it)
                }
            }

            // elimina i servizi salavti lato server che non sono più presenti in locale
            updateList = allServiceCredentialRemote.subtract(allServiceCredentialLocal)
            if(updateList.isNotEmpty()) {
                updateList.forEach {
                    ServiceCredentialApi.rootService.delServiceCredential(it)
                }
            }
        } catch (error: Exception) {
            Log.e(TAG, error.toString())
        }
    }

    suspend fun delRemoteServiceCredential(serviceCredentialModel: ServiceCredentialModel) {
        try {
            ServiceCredentialApi.rootService.delServiceCredential(
                this.toServiceCredentialRemoteModel(serviceCredentialModel)
            )
        } catch (error: Exception) {
            Log.e(TAG, error.toString())
        }
    }

    suspend fun delLocalServiceCredential(serviceCredentialModel: ServiceCredentialModel) {
        serviceCredentialLocal.deleteUtenza(
            this.toServiceCredential(serviceCredentialModel)
        )
    }

    suspend fun modRemoteServiceCredential(
        serviceCredentialModel: ServiceCredentialModel,
        oldServiceCredentialModel: ServiceCredentialModel
    ) {
        try {
            ServiceCredentialApi.rootService.modServiceCredential(
                OldServiceCredentialRemoteModel(
                    Service = serviceCredentialModel.service,
                    User = serviceCredentialModel.user,
                    Password = serviceCredentialModel.password,
                    OldService = oldServiceCredentialModel.service,
                    OldUser = oldServiceCredentialModel.user,
                    OldPassword = oldServiceCredentialModel.password
                )
            )
        } catch (error: Exception) {
            Log.e(TAG, error.toString())
        }
    }

    suspend fun modLocalServiceCredential(
        serviceCredentialModel: ServiceCredentialModel,
        oldServiceCredentialModel: ServiceCredentialModel
    ) {
        serviceCredentialLocal.updateUtenza(
            service = serviceCredentialModel.service,
            user = serviceCredentialModel.user,
            password = serviceCredentialModel.password,
            oldService = oldServiceCredentialModel.service,
            oldUser = oldServiceCredentialModel.user,
            oldPassword = oldServiceCredentialModel.password
        )
    }

    private fun toServiceCredentialRemoteModel(serviceCredentialModel : ServiceCredentialModel):
            ServiceCredentialRemoteModel {

        return ServiceCredentialRemoteModel (
            Service = serviceCredentialModel.service,
            User = serviceCredentialModel.user,
            Password = serviceCredentialModel.password
        )
    }

    private fun toServiceCredential(serviceCredential: ServiceCredentialModel):
            ServiceCredential {

        return ServiceCredential(
            Service = serviceCredential.service,
            User = serviceCredential.user,
            Password = serviceCredential.password
        )
    }
}