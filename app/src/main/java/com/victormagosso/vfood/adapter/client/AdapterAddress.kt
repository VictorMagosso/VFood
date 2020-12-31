package com.victormagosso.vfood.adapter.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victormagosso.vfood.R
import com.victormagosso.vfood.model.client.Address

class AdapterAddress(
    private val list: List<Address>?
) :
    RecyclerView.Adapter<AdapterAddress.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.adapter_address, parent, false
            )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val address = list!![position]

        holder?.let {
            it.address?.text = address.cAddress
            it.number?.text = address.nNumber.toString()
            it.neighborhood?.text = address.cNeighborhood
            it.state?.text = address.cState
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var address: TextView? = null
        var number: TextView? = null
        var neighborhood: TextView? = null
        var state: TextView? = null

        init {
            address = itemView.findViewById(R.id.txtSecondaryAddressName)
            number = itemView.findViewById(R.id.txtSecondaryAddressNumber)
            neighborhood = itemView.findViewById(R.id.txtSecondaryAddressNeighborhood)
            state = itemView.findViewById(R.id.txtSecondaryAddressState)
        }
    }
}