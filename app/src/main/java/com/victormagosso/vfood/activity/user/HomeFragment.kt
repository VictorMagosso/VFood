package com.victormagosso.vfood.activity.user

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.victormagosso.vfood.adapter.client.AdapterCategory
import com.victormagosso.vfood.adapter.client.AdapterCompanies
import com.victormagosso.vfood.adapter.company.AdapterMyProducts
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.category.Category
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.company.Product


class HomeFragment : Fragment() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    var dbRef: DatabaseReference = FirebaseConfig().getFirebaseDatabase()
    var userFirebase = UserFirebaseData()

    var companies: MutableList<Company> = mutableListOf()
    lateinit var adapterCompanies: AdapterCompanies

    var categories: MutableList<Category> = mutableListOf()
    lateinit var adapterCategory: AdapterCategory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(com.victormagosso.vfood.R.layout.fragment_home_user, container, false)

        var recyclerCompanies = view.findViewById<RecyclerView>(com.victormagosso.vfood.R.id.recyclerCompanies)
        var recyclerCategories = view.findViewById<RecyclerView>(com.victormagosso.vfood.R.id.recyclerCategory)

        createCompanyList()

        adapterCompanies = AdapterCompanies(companies)
        adapterCategory = AdapterCategory(createCategories())

        recyclerCompanies?.adapter = adapterCompanies
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerCompanies?.layoutManager = gridLayoutManager;
        recyclerCompanies?.hasFixedSize()

        recyclerCategories?.adapter = adapterCategory
        val gridLayoutManagerCategory = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerCategories?.layoutManager = gridLayoutManagerCategory;
        recyclerCategories?.hasFixedSize()

        return view
    }

    private fun createCategories(): List<Category> {
        return listOf(
            Category(com.victormagosso.vfood.R.drawable.sushi2, "Japonesa"),
            Category(com.victormagosso.vfood.R.drawable.burger, "Hambúrguer"),
            Category(com.victormagosso.vfood.R.drawable.french_fries, "Fast Food"),
            Category(com.victormagosso.vfood.R.drawable.salad, "Salada"),
            Category(com.victormagosso.vfood.R.drawable.barbecue, "Churrasco"),
            Category(com.victormagosso.vfood.R.drawable.pizza, "Pizza"),
            Category(com.victormagosso.vfood.R.drawable.kebab, "Árabe"),
            Category(com.victormagosso.vfood.R.drawable.homemade, "Caseiras"),
            Category(com.victormagosso.vfood.R.drawable.soda, "Bebidas"),
            Category(com.victormagosso.vfood.R.drawable.cake, "Sobremesas"),
        )
    }

    private fun createCompanyList() {
        var companiesRef: DatabaseReference = dbRef
            .child("companies")

        companiesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds: DataSnapshot in snapshot.children) {
                    companies?.add(ds.getValue(Company::class.java)!!)
                }
                adapterCompanies.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity?.applicationContext, "Até Logo!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}