package com.victormagosso.vfood.adapter.client

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.victormagosso.vfood.R
import com.victormagosso.vfood.model.order.ItemOrder

class AdapterCart() : RecyclerView.Adapter<AdapterCart.MyViewHolder>() {

    private var list = emptyList<ItemOrder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.adapter_cart, parent, false
            )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cart = list!![position]

        holder?.let {
            it.product?.text = cart.cProductName
            it.quantity?.text = cart.nQuantity.toString()
            it.price?.text = "R$ ${cart.nTotalPrice.toString().replace(".", ",")}"
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var product: TextView? = null
        var quantity: TextView? = null
        var price: TextView? = null

        init {
            product = itemView.findViewById(R.id.txtProductCartName)
            quantity = itemView.findViewById(R.id.txtProductCartQtt)
            price = itemView.findViewById(R.id.txtProductTotalValue)
        }
    }
    fun setData(item: List<ItemOrder>) {
        this.list = item
        notifyDataSetChanged()
    }
    fun getData(): List<ItemOrder> {
        return this.list
    }

}