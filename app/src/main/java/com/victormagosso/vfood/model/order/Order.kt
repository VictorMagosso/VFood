package com.victormagosso.vfood.model.order

class Order (
    var cIdOrder: String = "",
    var cIdCompany: String = "",
    var cIdClient: String = "",
    var cCompanyName: String = "",
    var cClientName: String = "",
    var cClientAddress: String = "",
    var cClientPhone: String = "",
    var cPayment: String = "",
    var nTotalPrice: Double = 0.0,
    var nStatus: Int = 0,
    var lItems: List<ItemOrder> = listOf()
)