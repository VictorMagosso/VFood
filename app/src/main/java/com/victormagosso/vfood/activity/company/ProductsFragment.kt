package com.victormagosso.vfood.activity.company

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.victormagosso.vfood.R
import com.victormagosso.vfood.adapter.AdapterLastAddedDishesMock
import com.victormagosso.vfood.adapter.AdapterMyProducts
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.*
import com.victormagosso.vfood.mockdata.mockmodels.LastAddedDishesMock
import com.victormagosso.vfood.model.company.Product

class ProductsFragment : Fragment() {
    var dbRef: DatabaseReference = FirebaseConfig().getFirebaseDatabase()
    var userFirebase = UserFirebaseData()
    var products: MutableList<Product> = mutableListOf()
    var adapterMyProducts = AdapterMyProducts(products)
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

        val swipe = object : SwipeHelper(activity!!.applicationContext, recyclerMyProducts, 200) {
            override fun instantiateButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<ButtonActions>
            ) {
                buffer.add(
                    ButtonActions(
                        activity!!.applicationContext,
                        "Delete",
                        30,
                        R.drawable.deleteicon,
                        Color.parseColor("#FF3C30"),
                        object : ButtonActionsClickListener {
                            override fun onClick(pos: Int) {
                                Toast.makeText(
                                    activity!!.applicationContext,
                                    "Aqui vc vai deletar " + pos,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    )
                )
                buffer.add(
                    ButtonActions(
                        activity!!.applicationContext,
                        "Update",
                        30,
                        R.drawable.editicon,
                        Color.parseColor("#F4F4F4"),
                        object : ButtonActionsClickListener {
                            override fun onClick(pos: Int) {
                                Toast.makeText(
                                    activity!!.applicationContext,
                                    "Aqui vc vai editar " + pos,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    )
                )
            }

        }
        createProductList()

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