package com.victormagosso.vfood.model.company

import com.google.firebase.database.Exclude

class Product {
    @get:Exclude
    var cIdProduct: String? = "";

    var cName: String? = "";

    var cDescription: String? = "";

    var nPrice: Double? = 0.0;

    var cImgUrl: String? = "";

    var bAvailable: Boolean? = true;
}