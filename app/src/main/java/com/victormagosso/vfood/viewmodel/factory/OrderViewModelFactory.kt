package com.victormagosso.vfood.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.victormagosso.vfood.repository.OrderRepository
import com.victormagosso.vfood.repository.ProductRepository
import com.victormagosso.vfood.viewmodel.OrderViewModel
import com.victormagosso.vfood.viewmodel.ProductViewModel

class OrderViewModelFactory (private val repository: OrderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OrderViewModel(repository) as T
    }
}