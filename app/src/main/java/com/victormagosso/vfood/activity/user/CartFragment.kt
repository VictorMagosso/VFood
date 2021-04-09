package com.victormagosso.vfood.activity.user

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.victormagosso.vfood.R
import com.victormagosso.vfood.adapter.client.AdapterCart
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.Base64Custom
import com.victormagosso.vfood.listener.RecyclerItemClickListener
import com.victormagosso.vfood.model.client.Address
import com.victormagosso.vfood.model.client.Client
import com.victormagosso.vfood.model.client.CreditCard
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.order.ItemOrder
import com.victormagosso.vfood.model.order.Order
import com.victormagosso.vfood.viewmodel.ItemOrderViewModel
import kotlin.math.absoluteValue

class CartFragment : Fragment() {
    private lateinit var itemOrderViewModel: ItemOrderViewModel

    var selectedCompany = Company()
    var clientName = ""
    var clientPhone = ""

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseConfig = FirebaseConfig()
    var base64Custom = Base64Custom()
    var itemsOrder: MutableList<ItemOrder> = mutableListOf()

    var orderService = OrderService()

    var uid = base64Custom.encodeToBase64(firebaseConfig.getFirebaseAuth().currentUser?.email!!)
    var counterIdOrder: Int = 0
    var orderId = ""

    private var addressRef = FirebaseConfig().getFirebaseDatabase()
        .child("addresses")
        .child(uid!!)
        .child("mainAddresses")

    var cardRef = FirebaseConfig().getFirebaseDatabase()
        .child("cards")
        .child(uid!!)

    var clientRef = FirebaseConfig().getFirebaseDatabase()
        .child("clients")
        .child(uid!!)

    var orderRef = FirebaseConfig().getFirebaseDatabase()
        .child("orders")
        .child(uid!!)

    var adapterCart = AdapterCart()

    var confirmOrder: Button? = null

    var orderPrice: Double = 0.0

    var paymentMethod: TextView? = null
    var addressCart: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View =
            inflater.inflate(R.layout.fragment_cart, container, false)

        var toolbar: Toolbar = view.findViewById(R.id.toolbar_user)
        toolbar.title = "Meu pedido"

        var dialog = Dialog(activity!!)
        dialog.setCancelable(true)
        var viewDialog: View =
            activity!!.layoutInflater.inflate(R.layout.dialog_confirm, null, false)

        paymentMethod = view.findViewById(R.id.txtPaymentCart)
        addressCart = view.findViewById(R.id.txtAddressCart)

        var recyclerCart = view.findViewById<RecyclerView>(R.id.recyclerCart)

        confirmOrder = view.findViewById(R.id.btnConfirmOrder)

        recyclerCart?.adapter = adapterCart
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerCart?.layoutManager = gridLayoutManager;
        recyclerCart?.hasFixedSize()

        addressRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.value != null
                ) {
                    var address: Address = snapshot.getValue(Address::class.java)!!
                    addressCart?.text =
                        "${address.cAddress}, nº ${address.nNumber}\n${address.cNeighborhood} - ${address.cState}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print("error")
            }
        })

        cardRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.children.first().value != null
                ) {

                    var card: CreditCard =
                        snapshot.children.first().getValue(CreditCard::class.java)!!
                    paymentMethod?.text =
                        "${card.cType} - * * * * ${card.cCardNumber?.takeLast(4)}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print("error")
            }
        })

        itemOrderViewModel = ViewModelProvider(this).get(ItemOrderViewModel::class.java)
        itemOrderViewModel.readAllData.observe(viewLifecycleOwner, Observer { item ->
            adapterCart.setData(item)
            orderPrice = item.sumByDouble { it.nTotalPrice!! }
            confirmOrder?.text = "CONFIRMAR - ${orderPrice}"
            itemsOrder = item as MutableList<ItemOrder>

            recyclerCart.addOnItemTouchListener(
                RecyclerItemClickListener(
                    activity,
                    recyclerCart,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View?, position: Int) {
                            print("not impl")
                        }

                        override fun onLongItemClick(view: View?, position: Int) {
                            dialog.setContentView(viewDialog)
                            var btnConfirm: Button =
                                viewDialog.findViewById(R.id.btnConfirmExclude);
                            var btnCancel: Button = viewDialog.findViewById(R.id.btnCancelExclude);

                            btnConfirm.setOnClickListener {
                                var selectedItem = adapterCart.getData()[position]
                                itemOrderViewModel.deleteItem(selectedItem)

                                Toast.makeText(
                                    activity,
                                    "Item removido",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                adapterCart.notifyDataSetChanged()
                                dialog.dismiss()
                            }
                            btnCancel.setOnClickListener {
                                dialog.dismiss()
                            }
                            dialog.show();
                        }
                    }
                )
            )
        })
        clientRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.value != null) {
                    var client: Client = snapshot.getValue(Client::class.java)!!
                    clientName = client.cClientName!!
                    clientPhone = client.cTelephone
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print("error")
            }
        })
        confirmOrder?.setOnClickListener {
            var bundle: Bundle = activity!!.intent.extras!!
            selectedCompany = bundle.getSerializable("company_selected_order") as Company

            incrementOrderId()

            var order = Order(
                cIdOrder = "${counterIdOrder}${uid}",
                cIdCompany = selectedCompany.cId!!,
                cIdClient = uid!!,
                cCompanyName = selectedCompany.cCompanyName!!,
                cClientName = clientName,
                cClientAddress = addressCart?.text.toString(),
                cClientPhone = clientPhone,
                cPayment = paymentMethod?.text.toString(),
                nTotalPrice = orderPrice,
                nStatus = 0,
                lItems = itemsOrder
            )

            orderService.saveOrder(order)
            Toast.makeText(activity, "Seu pedido será preparado em breve!", Toast.LENGTH_SHORT)
                .show()
        }
        return view
    }

    private fun incrementOrderId() {
        orderRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                counterIdOrder = snapshot.children.count().absoluteValue
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }

        })
    }
}