package com.victormagosso.vfood.adapter.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victormagosso.vfood.R
import com.victormagosso.vfood.model.client.Address
import com.victormagosso.vfood.model.client.CreditCard

class AdapterCreditCard(
    private val list: List<CreditCard>?
) :
    RecyclerView.Adapter<AdapterCreditCard.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.adapter_credit_card, parent, false
            )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val creditCard = list!![position]

        holder?.let {
            it.cardNumber?.text = "* * * * ${creditCard.cCardNumber?.takeLast(4)}"
            it.cardType?.text = creditCard.cType
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardNumber: TextView? = null
        var cardType: TextView? = null

        init {
            cardNumber = itemView.findViewById(R.id.txtCardLastNumbers)
            cardType = itemView.findViewById(R.id.txtCardType)
        }
    }
}