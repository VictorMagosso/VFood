package com.victormagosso.vfood.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.victormagosso.vfood.repository.ProductRepository
import com.victormagosso.vfood.viewmodel.ProductViewModel

class ProductViewModelFactory (private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductViewModel(repository) as T
    }
}