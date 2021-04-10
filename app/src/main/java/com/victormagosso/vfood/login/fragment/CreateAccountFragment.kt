package com.victormagosso.vfood.login.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.victormagosso.vfood.R

class CreateAccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_account, container, false)

        val goToLogin = view.findViewById<Button>(R.id.btnGoLogin)
        val createAccount = view.findViewById<Button>(R.id.btnCreateAccount)


        return view
    }
}