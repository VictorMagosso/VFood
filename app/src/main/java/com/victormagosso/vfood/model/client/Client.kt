package com.victormagosso.vfood.model.client

data class Client(
    var cId: String? = "",
    var cClientName: String? = "",
    var cType: String? = "U",
    var dDate: String = "",
    var cTelephone: String = "",
    var cEmail: String = "",
)