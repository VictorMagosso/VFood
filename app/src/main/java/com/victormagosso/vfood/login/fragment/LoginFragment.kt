package com.victormagosso.vfood.login.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Pulse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.victormagosso.vfood.R
import com.victormagosso.vfood.activity.user.UserActivity
import com.victormagosso.vfood.repository.ClientRepository
import com.victormagosso.vfood.repository.CompanyRepository
import com.victormagosso.vfood.viewmodel.ClientViewModel
import com.victormagosso.vfood.viewmodel.CompanyViewModel
import com.victormagosso.vfood.viewmodel.factory.ClientViewModelFactory
import com.victormagosso.vfood.viewmodel.factory.CompanyViewModelFactory
import java.util.*


class LoginFragment : Fragment() {
    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var progressBar: View
    private lateinit var btnSignInWithGoogle: Button

    private lateinit var clientViewModel: ClientViewModel
    private lateinit var companyViewModel: CompanyViewModel

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 1000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        init(view)

        btnSignInWithGoogle.setOnClickListener {
            userPassword.visibility = View.GONE
            userEmail.visibility = View.GONE
            signIn()
        }

        createRequest()

        var gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        btnSignIn.setOnClickListener {
            var email = userEmail.text.toString()
            var password = userPassword.text.toString()

            if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                clientViewModel.errorMsgLogin.value = ""
                clientViewModel.signInUsingEmailAndPassword(email, password)

                progressBar.visibility = View.VISIBLE
            }
            clientViewModel.isSuccessLogging.observe(requireActivity(), { success ->
                if (success!!) {
                    clientViewModel.getUser()
                    clientViewModel.userLiveData.observe(requireActivity(), {
                        Toast.makeText(
                            activity,
                            "Olá novamente, ${it.cClientName?.capitalize(Locale.ROOT)?.split(" ")?.get(0)}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    })
                    startActivity(Intent(activity, UserActivity::class.java))
                }
            })

            clientViewModel.errorMsgLogin.observe(viewLifecycleOwner, {
                if (it!!.isNotEmpty()) {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE

                }
            })
        }

        clientViewModel.isSuccessCreatingLogin.observe(viewLifecycleOwner, {
            if (it!!) {

            }
        })

        return view
    }
    private fun init(view: View) {
        initComponents(view)
        initViewModels()
    }

    private fun initComponents(view: View) {
        userEmail = view.findViewById(R.id.editUserLogin)
        userPassword = view.findViewById(R.id.editUserPassword)
        btnSignIn = view.findViewById(R.id.btnLogin)
        btnSignInWithGoogle = view.findViewById(R.id.btnSignInGoogle)
        progressBar = view.findViewById(R.id.progressLogin)
        val pulse: Sprite = Pulse()
        (progressBar as ProgressBar).indeterminateDrawable = pulse

    }

    private fun initViewModels() {
        clientViewModel = ViewModelProvider(
            this,
            ClientViewModelFactory(ClientRepository())
        ).get(ClientViewModel::class.java)

        companyViewModel = ViewModelProvider(
            this,
            CompanyViewModelFactory(CompanyRepository())
        ).get(CompanyViewModel::class.java)
    }

    private fun createRequest() {
        //Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //Build a GoogleSignInClient with the options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
//        startActivity(Intent(this, GoogleAuthActivity::class.java))
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                clientViewModel.firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                view?.let { Snackbar.make(it, "Algo deu errado. Por favor, tente novamente", 2000) }
            }
        }
    }

    private fun verifyUserType() {

    }
}