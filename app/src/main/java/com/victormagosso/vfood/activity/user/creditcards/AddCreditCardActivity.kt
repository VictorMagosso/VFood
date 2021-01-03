package com.victormagosso.vfood.activity.user.creditcards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import com.santalu.maskara.widget.MaskEditText
import com.victormagosso.vfood.R
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.Base64Custom
import com.victormagosso.vfood.model.client.CreditCard
import com.victormagosso.vfood.service.CreditCardService
import java.text.SimpleDateFormat
import java.util.*

class AddCreditCardActivity : AppCompatActivity() {
    var uEmail = FirebaseConfig().getFirebaseAuth().currentUser?.email
    var base64Custom = Base64Custom()
    var uid = base64Custom.encodeToBase64(uEmail!!)

    var txtCardNumber: TextView? = null
    var txtCardName: TextView? = null
    var txtCardValid: TextView? = null
    var txtCardType: TextView? = null

    var editCardNumber: MaskEditText? = null
    var editCardName: EditText? = null
    var editCardValid: MaskEditText? = null

    var btnConfirmCard: Button? = null

    var cardService = CreditCardService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_credit_card)

        var toolbar: Toolbar = findViewById(R.id.toolbar_user)
        toolbar.title = "Meu cartão"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        txtCardNumber = findViewById(R.id.txtCardNumber)
        txtCardName = findViewById(R.id.txtCardName)
        txtCardValid = findViewById(R.id.txtCardValid)
        txtCardType = findViewById(R.id.txtCardType)

        editCardNumber = findViewById(R.id.editCardNumber)
        editCardName = findViewById(R.id.editCardName)
        editCardValid = findViewById(R.id.editCardValid)

        btnConfirmCard = findViewById(R.id.btnConfirmCard)

        editCardNumber?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                print("Not Imple")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0) txtCardNumber?.text = ""

                txtCardNumber?.text = editCardNumber?.text

                when (editCardNumber?.text?.toString()?.take(1)) {
                    "4" -> txtCardType?.text = "VISA"
                    "5" -> txtCardType?.text = "MASTERCARD"
                    "_" -> txtCardType?.text = txtCardType?.text
                    else -> txtCardType?.text = ""
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                print("Not Imple")
            }

        })

        editCardName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                print("Not Imple")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0) txtCardName?.text = ""

                txtCardName?.text = editCardName?.text.toString().toUpperCase()
            }

            override fun afterTextChanged(p0: Editable?) {
                print("Not Imple")
            }

        })

        editCardValid?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                print("Not Imple")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0) txtCardValid?.text = ""

                txtCardValid?.text = editCardValid?.text
            }

            override fun afterTextChanged(p0: Editable?) {
                print("Not Imple")
            }

        })

        btnConfirmCard?.setOnClickListener {
            var cardName: String = editCardName?.text.toString()
            var cardNumber: String = editCardNumber?.text.toString()
            var cardValid: String = editCardValid?.text.toString()

            if (!cardNumber.isNullOrEmpty() &&
                !cardName.isNullOrEmpty() &&
                !cardValid.isNullOrEmpty()
            ) {
                var card = CreditCard()
                card.cCardName = cardName
                card.cCardNumber = cardNumber.replace(" ", "-")
                card.cCardValid = cardValid
                card.cIdCard = "CARTAO-${uid}${cardNumber.takeLast(4)}"
                card.dDate = SimpleDateFormat().format(Date())
                card.cType = txtCardType?.text.toString()

                cardService.saveCard(card)
                startActivity(Intent(this, CreditCardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Por favor, verifique os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}