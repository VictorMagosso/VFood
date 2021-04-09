package com.victormagosso.vfood.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.victormagosso.vfood.repository.AddressRepository
import com.victormagosso.vfood.viewmodel.AddressViewModel

class AddressViewModelFactory (private val repository: AddressRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddressViewModel(repository) as T
    }
}