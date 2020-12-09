package com.victormagosso.vfood.activity.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class AnalysisFragment : Fragment() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        var sair = view?.findViewById<Button>(com.victormagosso.vfood.R.id.logoff)
//        sair?.setOnClickListener {
//            auth.signOut()
//            activity?.finish()
//        }
        return inflater.inflate(com.victormagosso.vfood.R.layout.fragment_analysis_user, container, false)
    }
}