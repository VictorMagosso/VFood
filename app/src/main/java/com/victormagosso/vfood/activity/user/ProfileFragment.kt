package com.victormagosso.vfood.activity.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.victormagosso.vfood.R
import com.victormagosso.vfood.activity.user.address.AddressActivity
import com.victormagosso.vfood.activity.user.creditcards.CreditCardActivity
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.Base64Custom
import com.victormagosso.vfood.model.client.Address
import com.victormagosso.vfood.model.client.Client

class ProfileFragment : Fragment() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseConfig = FirebaseConfig()
    var base64Custom = Base64Custom()

    var txtFullNameUser: TextView? = null
    var txtEmailUser: TextView? = null
    var txtAddressSelected: TextView? = null
    var txtTel: TextView? = null

    var cardOpenAddress: CardView? = null
    var cardOpenCreditCard: CardView? = null

    var uid = base64Custom.encodeToBase64(firebaseConfig.getFirebaseAuth().currentUser?.email!!)
    var logout: Button? = null

    var dbRef = FirebaseConfig().getFirebaseDatabase()
        .child("clients")
        .child(uid!!)

    var addressRef = FirebaseConfig().getFirebaseDatabase()
        .child("addresses")
        .child(uid!!)
        .child("mainAddresses")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var profileView = inflater.inflate(R.layout.fragment_profile_user, container, false)

        logout = profileView?.findViewById(R.id.btnLogout)

        txtFullNameUser = profileView?.findViewById(R.id.txtFullNameUser)
        txtEmailUser = profileView?.findViewById(R.id.txtEmailUser)
        txtAddressSelected = profileView?.findViewById(R.id.txtAddressSelected)
        txtTel = profileView?.findViewById(R.id.txtTel)

        cardOpenAddress = profileView?.findViewById(R.id.cardAddress)

        cardOpenCreditCard = profileView?.findViewById(R.id.cardCreditCards)

        logout!!.setOnClickListener {
            auth.signOut()
            activity?.finish()
        }

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.value != null) {
                    var client: Client = snapshot.getValue(Client::class.java)!!
                    txtFullNameUser?.text = client.cClientName
                    txtEmailUser?.text = client.cEmail
                    txtTel?.text = client.cTelephone
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print("error")
            }
        })

        addressRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.value != null) {
                    var address: Address = snapshot.getValue(Address::class.java)!!
                    if (address.cAddress.isNullOrEmpty()) {
                        txtAddressSelected?.text = "Nenhum endereço cadastrado"
                    } else {
                        txtAddressSelected?.text =
                            "${address.cAddress}, nº ${address.nNumber}\n${address.cNeighborhood} - ${address.cState}"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print("error")
            }
        })

        cardOpenAddress?.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    AddressActivity::class.java
                )
            )
        }
        cardOpenCreditCard?.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    CreditCardActivity::class.java
                )
            )
        }

        return profileView
    }
}