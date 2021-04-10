package com.victormagosso.vfood.model.client

data class Address (
    var cIdAddress: String? = "",
    var cAddress: String? = "",
    var cCep: String? = "",
    var cComplement: String? = "",
    var nNumber: Int? = null,
    var cState: String? = "",
    var cNeighborhood: String? = "",
    var dDate: String = "",
)