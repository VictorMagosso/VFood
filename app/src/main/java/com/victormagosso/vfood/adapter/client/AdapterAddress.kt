package com.victormagosso.vfood.adapter.client

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.victormagosso.vfood.R

class AdapterAddress(
    private var context: Context,
    private var addressList: MutableList<String>,
            private var addressTopicsList:HashMap<String, List<String>>,
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return this.addressList.size
    }

    override fun getChildrenCount(p0: Int): Int {
        return this.addressTopicsList[this.addressList[p0]]!!.size
    }

    override fun getGroup(p0: Int): Any {
        return this.addressList[p0]
    }

    override fun getChild(p0: Int, p1: Int): Any {
        return this.addressTopicsList[this.addressList[p0]]!![p1]!!
    }

    override fun getGroupId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        return p1.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    @SuppressLint("InflateParams")
    override fun getGroupView(p0: Int, p1: Boolean, convertView: View?, p3: ViewGroup?): View {
        var convertView = convertView
        var addressTitle: String = getGroup(p0) as String

        if (convertView == null) {
            val inflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.address_list, null)
        }

        var address: TextView = convertView!!.findViewById(R.id.txtAddressOfList)
        address.text = addressTitle

        return convertView!!
    }

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, convertView: View?, p4: ViewGroup?): View {
        var convertView = convertView
        var addressTopicTitle: String = getChild(p0, p1) as String

        if (convertView == null) {
            val inflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.address_topics_list, null)
        }

        var addressTitle: TextView = convertView!!.findViewById(R.id.txtAddressTopics)
        addressTitle.text = addressTopicTitle

        return convertView!!
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }
}