package com.victormagosso.vfood.api

import com.victormagosso.vfood.service.CepService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiCEP {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: CepService by lazy {
        retrofit.create(CepService::class.java)
    }
}