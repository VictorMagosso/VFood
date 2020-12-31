package com.victormagosso.vfood.adapter.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.victormagosso.vfood.R
import com.victormagosso.vfood.model.category.Category
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.company.Product

class AdapterMenu(
    private val list: List<Product>?
) :
    RecyclerView.Adapter<AdapterMenu.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.adapter_menu, parent, false
            )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = list!![position]
        var imgUrl = product.cImgUrl

        //só pega a ultima imagem. POR QUE???????
        holder?.let {
            it.name?.text = product.cName
            it.description?.text = product.cDescription
            it.price?.text = "R$ " + product.nPrice.toString()
            if (imgUrl != null) Picasso.get().load(imgUrl).into(it.image)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var description: TextView? = null
        var price: TextView? = null
        var image: ImageView? = null

        init {
            name = itemView.findViewById(R.id.txtNameMenu)
            description = itemView.findViewById(R.id.txtDescMenu)
            price = itemView.findViewById(R.id.txtPriceMenu)
            image = itemView.findViewById(R.id.imgProcutMenu)
        }
    }

}