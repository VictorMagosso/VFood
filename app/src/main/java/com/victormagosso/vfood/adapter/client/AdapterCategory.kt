package com.victormagosso.vfood.adapter.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.victormagosso.vfood.R
import com.victormagosso.vfood.model.category.Category
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.company.Product

class AdapterCategory(
    private val list: List<Category>?
) :
    RecyclerView.Adapter<AdapterCategory.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.adapter_category, parent, false
            )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = list!![position]

        holder?.let {
            it.name?.text = category.cCategory
            it.image?.setImageResource(category.nImgCategory!!)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var image: ImageView? = null

        init {
            name = itemView.findViewById(R.id.txtNameCategory)
            image = itemView.findViewById(R.id.imgCategory)
        }
    }
}