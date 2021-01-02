package com.victormagosso.vfood.activity.user.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.victormagosso.vfood.R
import com.victormagosso.vfood.activity.user.menu.MenuActivity
import com.victormagosso.vfood.adapter.client.AdapterCart
import com.victormagosso.vfood.listener.RecyclerItemClickListener
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.order.ItemOrder
import com.victormagosso.vfood.viewmodel.ItemOrderViewModel

class CartActivity : AppCompatActivity() {
    private lateinit var itemOrderViewModel: ItemOrderViewModel
    var adapterCart = AdapterCart()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        var recyclerCart = findViewById<RecyclerView>(R.id.recyclerCart)

        recyclerCart?.adapter = adapterCart
        val gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        recyclerCart?.layoutManager = gridLayoutManager;
        recyclerCart?.hasFixedSize()

        itemOrderViewModel = ViewModelProvider(this).get(ItemOrderViewModel::class.java)
        itemOrderViewModel.readAllData.observe(this, Observer { item ->
            adapterCart.setData(item)

            recyclerCart.addOnItemTouchListener(
                RecyclerItemClickListener(
                    applicationContext,
                    recyclerCart,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View?, position: Int) {
                            var selectedItem = adapterCart.getData()[position]
                            Toast.makeText(applicationContext, "oi$position", Toast.LENGTH_SHORT).show()
                        }

                        override fun onLongItemClick(view: View?, position: Int) {
                            Toast.makeText(applicationContext, "FAZ NADA AINDA", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            )
        })


    }
}