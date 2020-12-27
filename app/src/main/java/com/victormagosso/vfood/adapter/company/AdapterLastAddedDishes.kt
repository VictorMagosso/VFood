package com.victormagosso.vfood.adapter.company

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victormagosso.vfood.mockdata.mockmodels.LastAddedDishesMock

class AdapterLastAddedDishesMock(
    private val list: List<LastAddedDishesMock>?) :
    RecyclerView.Adapter<AdapterLastAddedDishesMock.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(
                com.victormagosso.vfood.R.layout.adapter_last_added_dishes, parent, false
            )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val lastAddedDishesMock = list!![position]
        holder?.let {
            it.name?.text = lastAddedDishesMock.cName
            it.date?.text = lastAddedDishesMock.cDate
            it.image?.setImageResource(lastAddedDishesMock.nImage)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var image: ImageView? = null
        var date: TextView? = null

        init {
            name = itemView.findViewById(com.victormagosso.vfood.R.id.textFoodName)
            image = itemView.findViewById(com.victormagosso.vfood.R.id.imgFood)
            date = itemView.findViewById(com.victormagosso.vfood.R.id.textDateAdd)
        }
    }
}