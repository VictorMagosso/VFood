package com.victormagosso.vfood.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.victormagosso.vfood.repository.ClientRepository
import com.victormagosso.vfood.viewmodel.ClientViewModel

class ClientViewModelFactory (private val repository: ClientRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ClientViewModel(repository) as T
    }
}