package com.victormagosso.vfood.activity.user.address

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ExpandableListView
import androidx.appcompat.widget.Toolbar
import com.victormagosso.vfood.R
import com.victormagosso.vfood.adapter.client.AdapterAddress

class AddressActivity : AppCompatActivity() {
    lateinit var listViewAdapter: AdapterAddress
    lateinit var expandableListView: ExpandableListView
    private var addressList: MutableList<String> = mutableListOf()
    var addressTopicList: HashMap<String, List<String>> = linkedMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        var toolbar: Toolbar = findViewById(R.id.toolbar_user)
        toolbar.title = "Endereços"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        expandableListView = findViewById(R.id.listAddress)

        showList()

        listViewAdapter = AdapterAddress(this, addressList, addressTopicList)
        expandableListView.setAdapter(listViewAdapter)
    }

    private fun showList() {
        addressList.add("Endereço 1")
        addressList.add("Endereço 2")
        addressList.add("Endereço 3")

        var address1 = mutableListOf<String>()
        address1.add("Topico 1")
        address1.add("Topico 2")
        address1.add("Topico 3")

        var address2 = mutableListOf<String>()
        address2.add("Topico 1")
        address2.add("Topico 2")
        address2.add("Topico 3")

        var address3 = mutableListOf<String>()
        address3.add("Topico 1")
        address3.add("Topico 2")
        address3.add("Topico 3")

        addressTopicList.put(addressList[0], address1)
        addressTopicList.put(addressList[1], address2)
        addressTopicList.put(addressList[2], address3)
    }
}