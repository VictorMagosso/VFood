package com.victormagosso.vfood.service

import com.victormagosso.vfood.model.client.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CepService {
    @GET("{cep}/json")
    suspend fun getAddress(
        @Path("cep") cep: String,
    ): Response<ApiResponse>
}