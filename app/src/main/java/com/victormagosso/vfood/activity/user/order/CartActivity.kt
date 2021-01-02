package com.victormagosso.vfood.activity.user.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.victormagosso.vfood.R
import com.victormagosso.vfood.adapter.client.AdapterCart
import com.victormagosso.vfood.adapter.client.AdapterCompanies
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.order.ItemOrder

class CartActivity : AppCompatActivity() {
    var orderItems: MutableList<ItemOrder> = mutableListOf()
    lateinit var adapterCart: AdapterCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val bundle: Bundle = intent.extras!!
        val selectedItem = bundle.getParcelableArrayList<ItemOrder>("cartitems")
        selectedItem?.forEach {
            orderItems.add(it)
        }

//            items.add(selectedItems)
        var recyclerCart = findViewById<RecyclerView>(R.id.recyclerCart)

        adapterCart = AdapterCart(orderItems)

        recyclerCart?.adapter = adapterCart
        val gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        recyclerCart?.layoutManager = gridLayoutManager;
        recyclerCart?.hasFixedSize()

    }
}