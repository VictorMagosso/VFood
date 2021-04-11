package com.victormagosso.vfood.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.CubeGrid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.santalu.maskara.widget.MaskEditText
import com.victormagosso.vfood.R
import com.victormagosso.vfood.activity.company.CompanyActivity
import com.victormagosso.vfood.activity.user.UserActivity
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.Base64Custom
import com.victormagosso.vfood.model.client.Client
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.repository.ClientRepository
import com.victormagosso.vfood.repository.CompanyRepository


class AuthActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    var firebaseConfig = FirebaseConfig()
    var base64Custom = Base64Custom()
    private val companyRepository = CompanyRepository()
    private val clientRepository = ClientRepository()
    var company = Company()
    var client = Client()
    var userType = "C"



    private lateinit var userName: EditText
    private lateinit var userCpfCnpj: MaskEditText
    private lateinit var userTel: MaskEditText
    private lateinit var btnAccess: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_auth)

        val container = findViewById<RelativeLayout>(R.id.container)
        val progressBar = findViewById<View>(R.id.progress) as ProgressBar
        val cubeGrid: Sprite = CubeGrid()
        progressBar.indeterminateDrawable = cubeGrid

        val userPassword = findViewById<EditText>(R.id.editUserPassword)
        val repeatPassword = findViewById<EditText>(R.id.editUserRepeatPassword)
        val userEmail = findViewById<EditText>(R.id.editUserLogin)
        userCpfCnpj = findViewById(R.id.editCpfCnpj)
        userTel = findViewById(R.id.editUserTel)
        userName = findViewById(R.id.editUserName)
        val checkAction = findViewById<Switch>(R.id.switchAccess)
        btnAccess = findViewById(R.id.btnConcludeAction)
        val btnSignInWithGoogle = findViewById<Button>(R.id.btnSignInGoogle)
        val selectCompanyOrPerson = findViewById<RadioGroup>(R.id.selectCompanyOrPerson)
        val personSelected = findViewById<RadioButton>(R.id.radioPerson)
        val companySelected = findViewById<RadioButton>(R.id.radioCompany)

        //Request para o Google Auth



        auth = firebaseConfig.getFirebaseAuth()
        verifyLoggedUser()

        checkAction.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectCompanyOrPerson.visibility = View.VISIBLE
                repeatPassword.visibility = View.VISIBLE
                userCpfCnpj.visibility = View.VISIBLE
                userName.visibility = View.VISIBLE
                userName.visibility = View.VISIBLE
                userTel.visibility = View.VISIBLE

            } else {
                selectCompanyOrPerson.visibility = View.GONE
                repeatPassword.visibility = View.GONE
                userCpfCnpj.visibility = View.GONE
                userName.visibility = View.GONE
                userTel.visibility = View.GONE
            }
        }
        selectCompanyOrPerson.setOnCheckedChangeListener { _, _ ->
            if (personSelected.isChecked) {
                userName.hint = "Nome completo"
                userCpfCnpj.visibility = View.GONE
                userTel.visibility = View.VISIBLE
                userType = "U"
            }
            if (companySelected.isChecked) {
                userName.hint = "Nome da empresa"
                userCpfCnpj.visibility = View.VISIBLE
                userTel.visibility = View.GONE
                userCpfCnpj.hint = "CNPJ"
                userType = "C"
            }
        }
        if (checkAction.isChecked) {
            selectCompanyOrPerson.visibility = View.VISIBLE
            repeatPassword.visibility = View.VISIBLE
            userCpfCnpj.visibility = View.VISIBLE
            userName.visibility = View.VISIBLE
            if (personSelected.isChecked) {
                userName.hint = "Nome completo"
            } else {
                userName.hint = "Nome da empresa"
                userCpfCnpj.hint = "CNPJ"
            }
        }
    }

    override fun onStart() {
        super.onStart()

//        var user: FirebaseUser = auth?.currentUser!!
//        if(user!=null) {
//            Toast.makeText(this,"Tem alguem logad", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun verifyUserType(userEmail: String) {
//        progress.visibility = View.VISIBLE
        val uid = base64Custom.encodeToBase64(userEmail)
        val ref = firebaseConfig.getFirebaseDatabase()

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var companyPath = snapshot.child("companies").child(uid!!).child("ctype").value
                var userPath = snapshot.child("clients").child(uid!!).child("ctype").value

                if (companyPath == "C") startActivity(
                    Intent(
                        applicationContext,
                        CompanyActivity::class.java
                    )
                )

                if (userPath == "U") startActivity(
                    Intent(
                        applicationContext,
                        UserActivity::class.java
                    )
                )

//                if (userPath == null && companyPath == null)
//                    progress.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                print("error")
            }
        })
    }

    private fun verifyLoggedUser() {
        if (firebaseConfig.getFirebaseAuth().currentUser != null) {
            verifyUserType(firebaseConfig.getFirebaseAuth().currentUser!!.email!!)
        } else {
//            progress.visibility = View.GONE
        }
    }



    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

    }

}