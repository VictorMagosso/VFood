package com.victormagosso.vfood.activity.company

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import com.victormagosso.vfood.R
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.model.company.Product
import com.victormagosso.vfood.service.ProductService

class AddProductActivity : AppCompatActivity() {

    lateinit var editName: EditText
    var editDescription: EditText? = null
    var editPrice: EditText? = null
    var txtName: TextView? = null
    var txtPrice: TextView? = null
    var txtDescription: TextView? = null
    var availability: CheckBox? = null
    var productImg: ImageView? = null
    var status: Boolean = true

    var product = Product()
    var dbRef = FirebaseConfig().getFirebaseDatabase()
    private val productService = ProductService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        editName = findViewById(R.id.editProductName)
        editDescription = findViewById(R.id.editDescription)
        editPrice = findViewById(R.id.editPrice)
        txtName = findViewById(R.id.displayPrice)
        txtDescription = findViewById(R.id.displayDescription)
        txtName = findViewById(R.id.displayName)
        availability = findViewById(R.id.checkAvailable)
        productImg = findViewById(R.id.imgProduct)

//        editName.addTextChangedListener {charSequence ->
//            try {
//
//            } catch ()
//        }

    }
    fun saveProduct(view: View) {
        var name = editName?.text.toString()
        var description = editDescription?.text.toString()
        var price = editPrice?.text.toString()
        if (!name.isNullOrEmpty()) {
            if (!description.isNullOrEmpty()) {
                if (!price.isNullOrEmpty()) {
                    product.cIdProduct = name.replace(" ", "+")
                    product.cDescription = description
                    product.cName = name
                    product.nPrice = price.toDouble()

                    if (!availability?.isChecked!!) {
                        status = false
                    }

                    product.bAvailable = status
                    productService.saveProduct(product)
                    Toast.makeText(applicationContext, "Produto adicionado com sucesso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, CompanyActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Preencha o valor.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Preencha a descrição.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext, "Preencha o nome.", Toast.LENGTH_SHORT).show()
        }
    }

    fun goBack(view: View) {
        startActivity(Intent(applicationContext, CompanyActivity::class.java))
        finish()
    }
}