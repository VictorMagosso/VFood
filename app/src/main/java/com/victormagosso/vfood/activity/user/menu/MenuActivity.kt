package com.victormagosso.vfood.activity.user.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.squareup.picasso.Picasso
import com.victormagosso.vfood.R
import com.victormagosso.vfood.adapter.client.AdapterMenu
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.company.Product

class MenuActivity : AppCompatActivity() {
    var dbRef: DatabaseReference = FirebaseConfig().getFirebaseDatabase()
    private var search: MaterialSearchView? = null
    var selectedCompany = Company()
    var products: MutableList<Product> = mutableListOf()
    var adapterMenu = AdapterMenu(products)
    var idCompany: String = ""

    var imgCompanyMenu: ImageView? = null
    var nameCompanyMenu: TextView? = null
    var timeCompanyMenu: TextView? = null
    var descCompanyMenu: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        imgCompanyMenu = findViewById(R.id.imgCompanyMenu)
        nameCompanyMenu = findViewById(R.id.txtCompanyNameMenu)
        timeCompanyMenu = findViewById(R.id.txtTimeMenu)
        descCompanyMenu = findViewById(R.id.txtCategoryMenu)

        var bundle: Bundle = intent.extras!!
        if (bundle != null) {
            selectedCompany = bundle.getSerializable("company") as Company
            nameCompanyMenu?.text = selectedCompany.cCompanyName
            timeCompanyMenu?.text = "${selectedCompany.cTime} min."
            descCompanyMenu?.text = selectedCompany.cCategory
            idCompany = selectedCompany.cId!!

            var url: String = selectedCompany.cCompanyImg!!
            if (url.isNullOrEmpty()) {
                imgCompanyMenu?.setImageResource(R.drawable.productplaceholder)
            } else {
                Picasso.get().load(url).into(imgCompanyMenu)
            }
        } else {
            Toast.makeText(applicationContext, "Empresa indisponível =(", Toast.LENGTH_SHORT)
                .show()
        }

        var toolbar: Toolbar = findViewById(R.id.toolbar_user)
        toolbar.title = "Cardápio"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var recyclerMenu = findViewById<RecyclerView>(R.id.recyclerMenu)

        recyclerMenu?.adapter = adapterMenu
        val gridLayoutManager = GridLayoutManager(applicationContext, 1, GridLayoutManager.VERTICAL, false)
        recyclerMenu?.layoutManager = gridLayoutManager;
        recyclerMenu?.hasFixedSize()

        createMenuList()

        search = findViewById(R.id.searchViewUser)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_user, menu)

        var item: MenuItem = menu!!.findItem(R.id.search)
        search!!.setMenuItem(item)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun createMenuList() {
        var productsRef: DatabaseReference = dbRef
            .child("products")
            .child(idCompany)

        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds: DataSnapshot in snapshot.children) {
                    products?.add(ds.getValue(Product::class.java)!!)
                }
                adapterMenu.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}