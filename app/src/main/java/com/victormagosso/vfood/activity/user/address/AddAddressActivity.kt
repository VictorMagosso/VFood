package com.victormagosso.vfood.activity.user.address

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.victormagosso.vfood.R
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.Base64Custom
import com.victormagosso.vfood.model.client.Address
import com.victormagosso.vfood.repository.CepRepository
import com.victormagosso.vfood.viewmodel.CepViewModel
import com.victormagosso.vfood.viewmodel.factory.CepViewModelFactory
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer
import com.victormagosso.vfood.repository.AddressRepository

class AddAddressActivity : AppCompatActivity() {
    private lateinit var viewModel: CepViewModel

    var editCep: EditText? = null
    var editStreetName: EditText? = null
    var editNumber: EditText? = null
    var editState: EditText? = null
    var editNeighborhood: EditText? = null
    var editHomeReference: EditText? = null

    private var controlAddressType: String = ""
    private var dCreated: String? = ""

    private val currentUserUID = FirebaseConfig().getFirebaseAuth().currentUser!!.uid
    var uEmail = FirebaseConfig().getFirebaseAuth().currentUser?.email
    var base64Custom = Base64Custom()
    var uid = base64Custom.encodeToBase64(uEmail!!)

    private var address = Address()
    var dbRef = FirebaseConfig().getFirebaseDatabase()

    private val addressRepository = AddressRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        editCep = findViewById(R.id.editCep)
        editStreetName = findViewById(R.id.editStreetName)
        editNumber = findViewById(R.id.editNumber)
        editState = findViewById(R.id.editCityState)
        editNeighborhood = findViewById(R.id.editNeighborhood)
        editHomeReference = findViewById(R.id.editHomeReference)

        var bundle: Bundle = intent.extras!!
        if (bundle != null) {
            controlAddressType = bundle.get("controlAddressType").toString()
        }

        //se for principal, vai ficar ouvindo
        var mainAddressRef: DatabaseReference = dbRef
            .child("addresses")
            .child(uid!!)
            .child("mainAddresses")

        editCep?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                println("msg")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val repository = CepRepository()
                val viewModelFactory = CepViewModelFactory(repository)
                viewModel = ViewModelProvider(this@AddAddressActivity, viewModelFactory).get(CepViewModel::class.java)
                viewModel.getAddress(editCep?.text.toString().replace("-", ""))
                viewModel.myResponse.observe(this@AddAddressActivity, Observer{res ->
                    print("Retorno api " + res)
                    try {
                        if (res.isSuccessful)
                            editStreetName?.text =
                                Editable.Factory.getInstance().newEditable(res.body()?.logradouro)
                        editNeighborhood?.text = Editable.Factory.getInstance()
                            .newEditable(res.body()?.bairro)
                        editState?.text =
                            Editable.Factory.getInstance().newEditable("${res.body()?.localidade} - ${res.body()?.uf}")
                        editNumber?.hint = "Número"
                        editHomeReference?.hint = "Complemento"
                    } catch (e: Exception){
                        e.printStackTrace()
                        editStreetName?.text =
                            Editable.Factory.getInstance().newEditable("")
                        editNeighborhood?.text = Editable.Factory.getInstance()
                            .newEditable("")
                        editState?.text =
                            Editable.Factory.getInstance().newEditable("")
                    }
                })
            }

            override fun afterTextChanged(p0: Editable?) {
                print("msg")
            }

        })

        mainAddressRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    if (controlAddressType == "main") {
                        var address: Address = snapshot.getValue(Address::class.java)!!
                        editCep?.text =
                            Editable.Factory.getInstance().newEditable(address.cCep)
                        editStreetName?.text =
                            Editable.Factory.getInstance().newEditable(address.cAddress)
                        editNeighborhood?.text = Editable.Factory.getInstance()
                            .newEditable(address.cNeighborhood)
                        editNumber?.text = Editable.Factory.getInstance()
                            .newEditable(address.nNumber.toString())
                        editState?.text =
                            Editable.Factory.getInstance().newEditable(address.cState)
                        editHomeReference?.text =
                            Editable.Factory.getInstance().newEditable(address.cComplement)
                        dCreated = address.dDate
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print("nada faz")
            }
        })
//
//        if (controlAddressType == "main") {
//            saveChanges()
//        }

    }
    //se for endereço principal e ja tiver preenchido, entao vai salvar as alterações
    private fun saveChanges() {
        var streetName: String = editStreetName?.text.toString()
        var neighborhood: String = editNeighborhood?.text.toString()
        var number: String = editNumber?.text.toString()
        var state: String = editState?.text.toString()

        if (!streetName.isNullOrEmpty() &&
            !neighborhood.isNullOrEmpty() &&
            !number.isNullOrEmpty() &&
            !number.isNullOrEmpty()
        ) {
            address.cIdAddress = uid + streetName
            address.cAddress = streetName
            address.cNeighborhood = neighborhood
            address.cState = state
            address.nNumber = number.toInt()
            address.dDate = dCreated.toString()

            addressRepository.saveMainAddress(address)

            Toast.makeText(this?.applicationContext, "Endereço atualizado com sucesso!", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(
                this?.applicationContext,
                "Por favor, preencha os campos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun saveAddress(view: View) {
            var streetName = editStreetName?.text.toString()
            var number = editNumber?.text.toString()
            var stateCity = editState?.text.toString()
            var neighborhood = editNeighborhood?.text.toString()

            if (!streetName.isNullOrEmpty()) {
                if (!number.isNullOrEmpty()) {
                    if (!stateCity.isNullOrEmpty()) {
                        address.cIdAddress = uid + streetName
                        address.cAddress = streetName
                        address.nNumber = number.toInt()
                        address.cState = stateCity
                        address.cNeighborhood = neighborhood
                        address.dDate = SimpleDateFormat().format(Date())

                        when (controlAddressType) {
                            "secondary" -> addressRepository.saveSecondaryAddress(address)
                            "main" -> addressRepository.saveMainAddress(address)
                        }

                        Toast.makeText(applicationContext, "Endereço salvo", Toast.LENGTH_SHORT)
                            .show()

                        startActivity(Intent(applicationContext, AddressActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Verifique os campos",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Verifique os campos", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "Verifique os campos", Toast.LENGTH_SHORT).show()
            }
        }

        fun cancelAddAddress(view: View) {
            startActivity(Intent(applicationContext, AddressActivity::class.java))
            finish()
        }
    }
