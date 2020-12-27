package com.victormagosso.vfood.adapter.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.victormagosso.vfood.R
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.company.Product

class AdapterCompanies(
    private val list: List<Company>?) :
    RecyclerView.Adapter<AdapterCompanies.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.adapter_companies, parent, false
            )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val company = list!![position]
        var imgUrl = company.cCompanyImg

        holder?.let {
            it.name?.text = company.cCompanyName

            if (company.cCategory.isNullOrEmpty()) it.description?.text = "Sem descrição"
            else it.description?.text = company.cCategory

            if (company.cTime.isNullOrEmpty()) it.time?.text = "Sem estimativa"
            else it.time?.text = "${company.cTime} min."

            if (imgUrl.isNullOrEmpty())
                it.image?.setImageResource(R.drawable.productplaceholder)
            else
                Picasso.get().load(imgUrl).into(it.image)

        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var description: TextView? = null
        var time: TextView? = null
        var image: ImageView? = null

        init {
            name = itemView.findViewById(R.id.txtCompanyName)
            description = itemView.findViewById(R.id.txtDesc)
            time = itemView.findViewById(R.id.txtTime)
            image = itemView.findViewById(R.id.imgCompany)
        }
    }
}