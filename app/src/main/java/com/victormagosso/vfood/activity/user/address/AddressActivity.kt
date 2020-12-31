package com.victormagosso.vfood.activity.user.address

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.victormagosso.vfood.R
import com.victormagosso.vfood.activity.user.MenuActivity
import com.victormagosso.vfood.adapter.client.AdapterAddress
import com.victormagosso.vfood.adapter.company.AdapterMyProducts
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.client.Address
import com.victormagosso.vfood.model.client.Client
import com.victormagosso.vfood.model.client.MainAddress
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.company.Product
import com.victormagosso.vfood.service.AddressService
import com.victormagosso.vfood.service.ProductService
import kotlinx.android.synthetic.main.dialog_addresstype.*

class AddressActivity : AppCompatActivity() {
    var dbRef: DatabaseReference = FirebaseConfig().getFirebaseDatabase()
    var userFirebase = UserFirebaseData()
    var addresses: MutableList<Address> = mutableListOf()
    var adapterAddresses = AdapterAddress(addresses)

    //main address
    var txtMainAddress: TextView? = null
    var txtMainAddressNumber: TextView? = null
    var txtMainAddressNeighborhood: TextView? = null
    var txtMainAddressState: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        var toolbar: Toolbar = findViewById(R.id.toolbar_user)
        toolbar.title = "Endereços"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var recyclerAddress = findViewById<RecyclerView>(R.id.recyclerAddress)
        var addAddress = findViewById<FloatingActionButton>(R.id.fabAddAddress)

        txtMainAddress = findViewById(R.id.txtMainAddress)
        txtMainAddressNumber = findViewById(R.id.txtMainAddressNumber)
        txtMainAddressNeighborhood = findViewById(R.id.txtMainAddressNeighborhood)
        txtMainAddressState = findViewById(R.id.txtMainAddressState)

        recyclerAddress?.adapter = adapterAddresses
        val gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        recyclerAddress?.layoutManager = gridLayoutManager;
        recyclerAddress?.hasFixedSize()

        createAddressList()

        addAddress.setOnClickListener {
            var dialog = Dialog(this!!)
            dialog.setCancelable(true)
            var viewDialog: View =
                this!!.layoutInflater.inflate(R.layout.dialog_addresstype, null, false)

            dialog.setContentView(viewDialog)

            var btnMainAddress: Button = viewDialog.findViewById(R.id.btnMainAddress);
            var btnSecondaryAddress: Button = viewDialog.findViewById(R.id.btnSecondaryAddress);

            var intent = Intent(this, AddAddressActivity::class.java)

            btnMainAddress.setOnClickListener {
                intent.putExtra("controlAddressType", "main")
                startActivity(intent)
                dialog.dismiss()
            }
            btnSecondaryAddress.setOnClickListener {
                intent.putExtra("controlAddressType", "secondary")
                startActivity(intent)
                dialog.dismiss()
            }
            dialog.show();

        }

        //recupera os dados do endereço principal
        var mainAddressRef: DatabaseReference = dbRef
            .child("addresses")
            .child(userFirebase.getUid()!!)
            .child("mainAddresses")

        mainAddressRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    var address: Address = snapshot.getValue(Address::class.java)!!
                    txtMainAddress?.text = address.cAddress
                    txtMainAddressNumber?.text = address.nNumber?.toString()
                    txtMainAddressNeighborhood?.text = address.cNeighborhood
                    txtMainAddressState?.text = address.cState
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun createAddressList() {
        var secondaryAddressesRef: DatabaseReference = dbRef
            .child("addresses")
            .child(userFirebase.getUid()!!)
            .child("secondaryAddresses")

        secondaryAddressesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds: DataSnapshot in snapshot.children) {
                    addresses?.add(ds.getValue(Address::class.java)!!)
                }
                adapterAddresses.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}