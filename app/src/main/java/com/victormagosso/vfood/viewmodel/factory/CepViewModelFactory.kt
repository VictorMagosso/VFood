package com.victormagosso.vfood.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.victormagosso.vfood.repository.CepRepository
import com.victormagosso.vfood.viewmodel.CepViewModel

class CepViewModelFactory (private val repository: CepRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CepViewModel(repository) as T
    }
}