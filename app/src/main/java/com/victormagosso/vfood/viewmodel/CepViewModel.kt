package com.victormagosso.vfood.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victormagosso.vfood.model.client.ApiResponse
import com.victormagosso.vfood.repository.CepRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CepViewModel(private val repository: CepRepository): ViewModel() {
    val myResponse: MutableLiveData<Response<ApiResponse>> = MutableLiveData()

    fun getAddress(cep: String) {
        viewModelScope.launch{
            val response: Response<ApiResponse> = repository.getAddress(cep)
            myResponse.value = response
        }
    }
}