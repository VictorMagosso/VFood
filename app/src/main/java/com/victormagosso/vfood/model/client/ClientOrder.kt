package com.victormagosso.vfood.model.client

data class ClientOrder (
    var nOrder: Int? = 0,
    var cOrderDetails: String? = "",
    var cCompanyResponsible: String? = "",
    var bStatus: Boolean? = false
)