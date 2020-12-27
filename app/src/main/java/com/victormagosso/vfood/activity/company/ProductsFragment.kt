package com.victormagosso.vfood.activity.company

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.victormagosso.vfood.R
import com.victormagosso.vfood.adapter.AdapterMyProducts
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.*
import com.victormagosso.vfood.listener.RecyclerItemClickListener
import com.victormagosso.vfood.model.company.Product
import com.victormagosso.vfood.service.ProductService


class ProductsFragment : Fragment() {
    var dbRef: DatabaseReference = FirebaseConfig().getFirebaseDatabase()
    var userFirebase = UserFirebaseData()
    var products: MutableList<Product> = mutableListOf()
    var adapterMyProducts = AdapterMyProducts(products)
    var productService = ProductService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_dishes, container, false)
        var recyclerMyProducts = view.findViewById<RecyclerView>(R.id.recyclerMyProducts)

        recyclerMyProducts?.adapter = adapterMyProducts
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerMyProducts?.layoutManager = gridLayoutManager;
        recyclerMyProducts?.hasFixedSize()

        createProductList()

        recyclerMyProducts.addOnItemTouchListener(
            RecyclerItemClickListener(
                activity?.applicationContext,
                recyclerMyProducts,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        var dialog = Dialog(activity!!)
                        dialog.setCancelable(true)
                        var viewDialog: View =
                            activity!!.layoutInflater.inflate(R.layout.dialog_confirm, null, false)

                        dialog.setContentView(viewDialog)

                        var btnConfirm: Button = viewDialog.findViewById(R.id.btnConfirmExclude);
                        var btnCancel: Button = viewDialog.findViewById(R.id.btnCancelExclude);

                        btnConfirm.setOnClickListener {
                            var selectedProduct: Product = products[position]
                            productService.deleteProduct(selectedProduct)
                            Toast.makeText(activity, "Excluído com sucesso", Toast.LENGTH_SHORT)
                                .show()
                            adapterMyProducts.notifyDataSetChanged()
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

        return view
    }

    private fun createProductList() {
        var productsRef: DatabaseReference = dbRef
            .child("products")
            .child(userFirebase.getUid()!!)

        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds: DataSnapshot in snapshot.children) {
                    products?.add(ds.getValue(Product::class.java)!!)
                }
                adapterMyProducts.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}