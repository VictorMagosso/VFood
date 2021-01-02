package com.victormagosso.vfood.model.order

class Order {
    var cIdOrder: String = ""
    var cIdCompany: String = ""
    var cIdCliente: String = ""
    var cCompanyName: String = ""
    var cClientName: String = ""
    var cClienteAddress: String = ""
    var cClientePhone: String = ""
    var cPayment: String = ""
    var nTotalPrice: Double = 0.0
    var nStatus: Int = 0
    var lItems: MutableList<ItemOrder> = mutableListOf()
}