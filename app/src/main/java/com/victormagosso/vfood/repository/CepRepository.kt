package com.victormagosso.vfood.repository

import com.victormagosso.vfood.api.ApiCEP
import com.victormagosso.vfood.model.client.ApiResponse
import retrofit2.Response

class CepRepository {
    suspend fun getAddress(cep: String): Response<ApiResponse> {
        return ApiCEP.api.getAddress(cep)
    }
}