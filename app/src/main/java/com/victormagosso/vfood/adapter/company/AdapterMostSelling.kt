package com.victormagosso.vfood.adapter.company

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victormagosso.vfood.mockdata.mockmodels.MostSellingMock

class AdapterMostSelling(
    private val list: List<MostSellingMock>?) :
    RecyclerView.Adapter<AdapterMostSelling.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(
                com.victormagosso.vfood.R.layout.adapter_most_selling, parent, false
            )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mostSelling = list!![position]
        holder?.let {
            it.name?.text = mostSelling.cName
            it.date?.text = mostSelling.cLastTime
            it.times?.text = mostSelling.cTimes
            it.image?.setImageResource(mostSelling.nImage!!)
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

        init {
            name = itemView.findViewById(com.victormagosso.vfood.R.id.textName)
            times = itemView.findViewById(com.victormagosso.vfood.R.id.textQuantity)
            image = itemView.findViewById(com.victormagosso.vfood.R.id.imgMostSelling)
            date = itemView.findViewById(com.victormagosso.vfood.R.id.textLastDate)
        }
    }
}