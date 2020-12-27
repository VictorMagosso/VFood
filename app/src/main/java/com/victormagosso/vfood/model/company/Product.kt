package com.victormagosso.vfood.model.company

import com.google.firebase.database.Exclude

class Product() {
    var cIdProduct: String? = "";

    var cName: String? = "";

    var cDescription: String? = "";

    var nPrice: Double? = 0.0;

    var cImgUrl: String? = "";

    var dDate: String? = "";

    var cTimeSold: Int = 0

    var bAvailable: Boolean? = true;
}