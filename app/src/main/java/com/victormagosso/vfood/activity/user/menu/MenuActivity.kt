package com.victormagosso.vfood.activity.user.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.CubeGrid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.squareup.picasso.Picasso
import com.victormagosso.vfood.R
import com.victormagosso.vfood.adapter.client.AdapterMenu
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.Base64Custom
import com.victormagosso.vfood.listener.RecyclerItemClickListener
import com.victormagosso.vfood.model.client.Client
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.company.Product
import kotlinx.android.synthetic.main.activity_auth.*

class MenuActivity : AppCompatActivity() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseConfig = FirebaseConfig()
    var base64Custom = Base64Custom()

    var uid = base64Custom.encodeToBase64(firebaseConfig.getFirebaseAuth().currentUser?.email!!)

    var dbRef: DatabaseReference = FirebaseConfig().getFirebaseDatabase()

    var clientRef: DatabaseReference = dbRef
        .child("clients")
        .child(uid!!)

    private var search: MaterialSearchView? = null
    var selectedCompany = Company()
    var products: MutableList<Product> = mutableListOf()
    var adapterMenu = AdapterMenu(products)
    var idCompany: String = ""

    var client = Client()

    var imgCompanyMenu: ImageView? = null
    var nameCompanyMenu: TextView? = null
    var timeCompanyMenu: TextView? = null
    var descCompanyMenu: TextView? = null

    var progressBar:View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        imgCompanyMenu = findViewById(R.id.imgCompanyMenu)
        nameCompanyMenu = findViewById(R.id.txtCompanyNameMenu)
        timeCompanyMenu = findViewById(R.id.txtTimeMenu)
        descCompanyMenu = findViewById(R.id.txtCategoryMenu)

        progressBar = findViewById(R.id.progressMenu)
        val cubeGrid: Sprite = CubeGrid()
        (progressBar as ProgressBar).indeterminateDrawable = cubeGrid

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
        getClientData()

        search = findViewById(R.id.searchViewUser)

        recyclerMenu.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                recyclerMenu,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        var selectedProduct: Product = products[position]
                        var intent = Intent(applicationContext, ConfirmFoodActivity::class.java)
                        intent.putExtra("product", selectedProduct)
                        startActivity(intent)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        Toast.makeText(applicationContext, "FAZ NADA AINDA", Toast.LENGTH_SHORT).show()
                    }

                }
            )
        )
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
                print("Not implemented")
            }
        })
    }

    private fun getClientData() {
        clientRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    client = snapshot.getValue(Client::class.java)!!
                }
                getOrder()
            }

            override fun onCancelled(error: DatabaseError) {
                print("Not implemented")
            }

        })
    }
    private fun getOrder() {
        progressBar?.visibility = View.GONE
    }
}