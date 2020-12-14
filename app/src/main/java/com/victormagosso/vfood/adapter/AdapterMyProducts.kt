package com.victormagosso.vfood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.victormagosso.vfood.model.company.Product

class AdapterMyProducts(
    private val list: List<Product>?) :
    RecyclerView.Adapter<AdapterMyProducts.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(
                com.victormagosso.vfood.R.layout.adapter_my_products, parent, false
            )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = list!![position]
        var imgUrl = product.cImgUrl

        holder?.let {
            it.name?.text = product.cName
            it.date?.text = product.dDate
            it.times?.text = product.cTimeSold.toString()
            it.price?.text = "R$ " + product.nPrice.toString()
            if (imgUrl != null) Picasso.get().load(imgUrl).into(it.image)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var times: TextView? = null
        var image: ImageView? = null
        var date: TextView? = null
        var price: TextView? = null
        var score: ImageView? = null

        init {
            name = itemView.findViewById(com.victormagosso.vfood.R.id.textProductName)
            times = itemView.findViewById(com.victormagosso.vfood.R.id.textQuantitySold)
            image = itemView.findViewById(com.victormagosso.vfood.R.id.imgProduct)
            date = itemView.findViewById(com.victormagosso.vfood.R.id.textLastSold)
            price = itemView.findViewById(com.victormagosso.vfood.R.id.textPrice)
            score = itemView.findViewById(com.victormagosso.vfood.R.id.imgAverage)

        }

    }
}