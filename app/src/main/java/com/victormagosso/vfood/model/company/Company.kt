package com.victormagosso.vfood.model.company

import java.io.Serializable

data class Company(
    var cId: String? = "",
    var cCategory: String? = "",
    var cCompanyName: String? = "",
    var cCompanyImg: String? = "",
    var cType: String? = "C",
    var dDate: String? = "",
    var cTime: String? = "",
    var cEmail: String? = "",
    var cDocument: String? = "",
    var cAddress: String? = "",
    var cPhone: String? = "",
) : Serializable
