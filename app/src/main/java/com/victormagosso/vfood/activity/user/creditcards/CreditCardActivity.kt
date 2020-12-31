package com.victormagosso.vfood.activity.user.creditcards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.victormagosso.vfood.R
import com.victormagosso.vfood.adapter.client.AdapterCreditCard
import com.victormagosso.vfood.adapter.company.AdapterMyProducts
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.client.CreditCard
import com.victormagosso.vfood.model.company.Product

class CreditCardActivity : AppCompatActivity() {
    var dbRef: DatabaseReference = FirebaseConfig().getFirebaseDatabase()
    var userFirebase = UserFirebaseData()

    var addCard: FloatingActionButton? = null

    var cards: MutableList<CreditCard> = mutableListOf()
    var adapterCards = AdapterCreditCard(cards)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_card)

        var toolbar: Toolbar = findViewById(R.id.toolbar_user)
        toolbar.title = "Formas de pagamento"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var recyclerCards = findViewById<RecyclerView>(R.id.recyclerCards)

        recyclerCards?.adapter = adapterCards
        val gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        recyclerCards?.layoutManager = gridLayoutManager;
        recyclerCards?.hasFixedSize()

        addCard = findViewById(R.id.fabAddCard)

        addCard?.setOnClickListener {
            startActivity(Intent(this, AddCreditCardActivity::class.java))
        }

        createCardsList()
    }

    private fun createCardsList() {
        var cardsRef: DatabaseReference = dbRef
            .child("cards")
            .child(userFirebase.getUid()!!)

        cardsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds: DataSnapshot in snapshot.children) {
                    cards?.add(ds.getValue(CreditCard::class.java)!!)
                }
                adapterCards.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}