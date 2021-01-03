package com.victormagosso.vfood.activity.user.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.victormagosso.vfood.R
import com.victormagosso.vfood.activity.user.CartFragment
import com.victormagosso.vfood.activity.user.UserActivity
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.company.Product
import com.victormagosso.vfood.model.order.ItemOrder
import com.victormagosso.vfood.viewmodel.ItemOrderViewModel
import java.text.DecimalFormat

class ConfirmFoodActivity : AppCompatActivity() {
    var selectedProduct = Product()
    var selectedCompany = Company()
    var orderItems: ArrayList<ItemOrder> = arrayListOf()
    private lateinit var itemOrderViewModel: ItemOrderViewModel

    var companiesRef = FirebaseConfig().getFirebaseDatabase()
        .child("companies")

    var imgSelectedProduct: ImageView? = null
    var txtProduct: TextView? = null
    var txtDesc: TextView? = null
    var txtPrice: TextView? = null
    var editObs: EditText? = null
    var addToCart: CardView? = null
    var companyName: String? = ""

    var decFormat = DecimalFormat("#.##")
    var priceFormatted: Double? = 0.0

    var addQtt: TextView? = null
    var removeQtt: TextView? = null
    var qttChosen: TextView? = null
    var totalOrderPrice: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_food)

        itemOrderViewModel = ViewModelProvider(this).get(ItemOrderViewModel::class.java)

        var toolbar: Toolbar = findViewById(R.id.toolbar_user)
        toolbar.title = "Fazer pedido"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imgSelectedProduct = findViewById(R.id.imgSelectedProduct)
        txtProduct = findViewById(R.id.txtProduct)
        txtDesc = findViewById(R.id.txtDesc)
        txtPrice = findViewById(R.id.txtPrice)
        editObs = findViewById(R.id.editObservation)
        addToCart = findViewById(R.id.cardAddToCart)

        addQtt = findViewById(R.id.txtAdd)
        removeQtt = findViewById(R.id.txtSub)
        qttChosen = findViewById(R.id.txtQtt)
        totalOrderPrice = findViewById(R.id.txtTotal)

        var qttChosenInt = qttChosen?.text.toString().toInt()

        var bundle: Bundle = intent.extras!!
        if (bundle != null) {
            selectedProduct = bundle.getSerializable("product") as Product
            selectedCompany = bundle.getSerializable("company_details") as Company

            txtProduct?.text = selectedProduct.cName
            txtDesc?.text = selectedProduct.cDescription
            txtPrice?.text = "R$ ${selectedProduct.nPrice}"
                .replace(".", ",")

            totalOrderPrice?.text = txtPrice?.text

            //não está funcionando. Arredondar para 2 casa decimais
//            priceFormatted = decFormat.format(selectedProduct.nPrice).toDouble()

            var url: String = selectedProduct.cImgUrl!!
            if (url.isNullOrEmpty()) {
                imgSelectedProduct?.setImageResource(R.drawable.productplaceholder)
            } else {
                Picasso.get().load(url).into(imgSelectedProduct)
            }
        } else {
            Toast.makeText(applicationContext, "Produto indisponível =(", Toast.LENGTH_SHORT)
                .show()
        }

        qttChosen?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                print("nothing")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                qttChosenInt = qttChosen?.text.toString().toInt()
                totalOrderPrice?.text = "R$ ${selectedProduct.nPrice!! * qttChosenInt}"
                    .replace(".", ",")


            }

            override fun afterTextChanged(p0: Editable?) {
                print("nothing")
            }

        })

        addQtt?.setOnClickListener {
            qttChosen?.text = (qttChosenInt + 1).toString()
        }

        removeQtt?.setOnClickListener {
            if (qttChosenInt > 1)
                qttChosen?.text = (qttChosenInt - 1).toString()
        }

        addToCart?.setOnClickListener {
            var item = ItemOrder(
                0,
                selectedProduct.cIdProduct!!,
                selectedProduct.cName!!,
                qttChosen?.text.toString().toInt(),
                totalOrderPrice?.text!!
                    .toString()
                    .replace(",", ".")
                    .replace("[R$ ]".toRegex(), "")
                    .trim()
                    .toDouble(),
                editObs?.text.toString(),
            )
            itemOrderViewModel.addItem(item)
            Toast.makeText(this, "Adicionado ao carrinho", Toast.LENGTH_SHORT).show()
            var intent = Intent(applicationContext, UserActivity::class.java)
            intent.putExtra("company_selected_order", selectedCompany)
            startActivity(intent)
        }
    }
}