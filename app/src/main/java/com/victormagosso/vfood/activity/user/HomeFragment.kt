package com.victormagosso.vfood.activity.user

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.victormagosso.vfood.adapter.client.AdapterCompanies
import com.victormagosso.vfood.adapter.company.AdapterMyProducts
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.company.Product


class HomeFragment : Fragment() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    var dbRef: DatabaseReference = FirebaseConfig().getFirebaseDatabase()
    var userFirebase = UserFirebaseData()

    var companies: MutableList<Company> = mutableListOf()
    lateinit var adapterCompanies: AdapterCompanies

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(com.victormagosso.vfood.R.layout.fragment_home_user, container, false)
        var recyclerCompanies = view.findViewById<RecyclerView>(com.victormagosso.vfood.R.id.recyclerCompanies)
        createCompanyList()

        adapterCompanies = AdapterCompanies(companies)

        recyclerCompanies?.adapter = adapterCompanies
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerCompanies?.layoutManager = gridLayoutManager;
        recyclerCompanies?.hasFixedSize()


        return view
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
                TODO("Not yet implemented")
            }
        })
    }
}