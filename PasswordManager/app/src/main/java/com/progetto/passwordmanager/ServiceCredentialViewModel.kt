package com.progetto.passwordmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.progetto.passwordmanager.data.ServiceCredentialRepository
import com.progetto.passwordmanager.data.model.ServiceCredentialModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ServiceCredentialViewModel (private val repository: ServiceCredentialRepository) : ViewModel() {

    private val _uiSericeCedentialModelList = MutableStateFlow<List<ServiceCredentialModel>>(listOf())
    val uiServiceCredentialModelList : Flow<List<ServiceCredentialModel>> = _uiSericeCedentialModelList

    fun insert(serviceCredentialModel: ServiceCredentialModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLocalServiceCredential(serviceCredentialModel)

            val currentServiceCredential = repository.getAllLocalServiceCredential()
            val updateServiceCredentialList = mutableListOf<ServiceCredentialModel>()
            currentServiceCredential.forEach {
                updateServiceCredentialList.add(it)
            }
            _uiSericeCedentialModelList.emit(currentServiceCredential)

            repository.insertRemoteServiceCredential(serviceCredentialModel)
        }
    }

    fun getAll() {
        viewModelScope.launch(Dispatchers.IO) {
            val resLocal = repository.getAllLocalServiceCredential()
            _uiSericeCedentialModelList.emit(resLocal)

            /*
            val resRemote = repository.getAllServiceRemote()
            if (resRemote != null) {
                _uiSericeCedentialModelList.emit(resRemote)
            }*/
        }
    }

    fun updateServiceCredentialRemote() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateServiceCredentialRemote()
            _uiSericeCedentialModelList.emit(repository.getAllLocalServiceCredential())
        }
    }

    fun delServiceCredential(serviceCredentialModel: ServiceCredentialModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delLocalServiceCredential(serviceCredentialModel)
            this@ServiceCredentialViewModel.getAll()
            repository.delRemoteServiceCredential(serviceCredentialModel)
        }
    }

    fun modServiceCredential(serviceCredentialModel: ServiceCredentialModel,
                             oldServiceCredentialModel: ServiceCredentialModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.modLocalServiceCredential(serviceCredentialModel, oldServiceCredentialModel)
            this@ServiceCredentialViewModel.getAll()
            repository.modRemoteServiceCredential(serviceCredentialModel, oldServiceCredentialModel)
        }
    }
}

class ServiceCredentialViewModelFactory(private val repository: ServiceCredentialRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceCredentialViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServiceCredentialViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}