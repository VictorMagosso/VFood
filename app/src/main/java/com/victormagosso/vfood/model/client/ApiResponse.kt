package com.victormagosso.vfood.model.client

import com.google.gson.annotations.Expose

data class ApiResponse(
    @Expose
    val cep: String,
    val logradouro: String,
    val complemento: String,
    val bairro: String,
    val localidade: String,
    val uf: String,
    val ibge: String,
    val gia: String,
    val ddd: String,
    val siafi: String,
)
