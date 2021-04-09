package com.victormagosso.vfood.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.victormagosso.vfood.repository.CompanyRepository
import com.victormagosso.vfood.viewmodel.CompanyViewModel

class CompanyViewModelFactory (private val repository: CompanyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CompanyViewModel(repository) as T
    }
}