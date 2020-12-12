package com.victormagosso.vfood.model.company

import com.google.firebase.database.Exclude

class Company {
    @get:Exclude
    var cId: String? = "";

    var cCategory: String? = "";

    var cCompanyName: String? = "";

    var cCompanyImg: String? = "";

    var cType: String? = "C";

    var dDate: String? = "";

    var cTime: String? = "";

    var cEmail: String? = "";

    var cDocument: String? = "";
}
