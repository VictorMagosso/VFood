package com.victormagosso.vfood.activity.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.victormagosso.vfood.R

class ProfileFragment : Fragment() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var logout: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var profileView = inflater.inflate(R.layout.fragment_profile_user, container, false)

        logout = profileView?.findViewById(R.id.btnLogout)

        logout!!.setOnClickListener {
            auth.signOut()
            activity?.finish()
        }
        return profileView
    }
}