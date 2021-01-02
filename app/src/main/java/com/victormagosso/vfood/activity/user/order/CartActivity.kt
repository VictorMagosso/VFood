package com.victormagosso.vfood.activity.user.order

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.victormagosso.vfood.R
import com.victormagosso.vfood.adapter.client.AdapterCart
import com.victormagosso.vfood.listener.RecyclerItemClickListener
import com.victormagosso.vfood.viewmodel.ItemOrderViewModel

class CartActivity : AppCompatActivity() {
    private lateinit var itemOrderViewModel: ItemOrderViewModel
    var adapterCart = AdapterCart()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        var dialog = Dialog(this@CartActivity!!)
        dialog.setCancelable(true)
        var viewDialog: View =
            this@CartActivity.layoutInflater.inflate(R.layout.dialog_confirm, null, false)

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
                            print("not impl")
                        }

                        override fun onLongItemClick(view: View?, position: Int) {

                            dialog.setContentView(viewDialog)

                            var btnConfirm: Button = viewDialog.findViewById(R.id.btnConfirmExclude);
                            var btnCancel: Button = viewDialog.findViewById(R.id.btnCancelExclude);

                            btnConfirm.setOnClickListener {
                                var selectedItem = adapterCart.getData()[position]
                                itemOrderViewModel.deleteItem(selectedItem)

                                Toast.makeText(this@CartActivity, "Item removido com sucesso", Toast.LENGTH_SHORT)
                                    .show()
                                adapterCart.notifyDataSetChanged()
                                dialog.dismiss()
                            }
                            btnCancel.setOnClickListener {
                                dialog.dismiss()
                            }
                            dialog.show();
                        }
                    }
                )
            )
        })
    }
}