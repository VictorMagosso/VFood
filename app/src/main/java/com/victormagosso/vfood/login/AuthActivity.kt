package com.victormagosso.vfood.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.CubeGrid
import com.github.ybq.android.spinkit.style.RotatingPlane
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.victormagosso.vfood.R
import com.victormagosso.vfood.activity.company.CompanyActivity
import com.victormagosso.vfood.activity.user.UserActivity
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.Base64Custom
import com.victormagosso.vfood.model.client.Client
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.service.ClientService
import com.victormagosso.vfood.service.CompanyService
import kotlinx.android.synthetic.main.activity_auth.*
import java.text.SimpleDateFormat
import java.util.*


class AuthActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var firebaseConfig = FirebaseConfig()
    var base64Custom = Base64Custom()
    private val companyService = CompanyService()
    private val clientService = ClientService()
    var company = Company()
    var client = Client()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_auth)

        val container = findViewById<RelativeLayout>(R.id.container)
        val progressBar = findViewById<View>(R.id.progress) as ProgressBar
        val cubeGrid: Sprite = CubeGrid()
        progressBar.indeterminateDrawable = cubeGrid
        container?.visibility = View.GONE

        var userType = "C"
        val userPassword = findViewById<EditText>(R.id.editUserPassword)
        val repeatPassword = findViewById<EditText>(R.id.editUserRepeatPassword)
        val userEmail = findViewById<EditText>(R.id.editUserLogin)
        val userCpfCnpj = findViewById<EditText>(R.id.editCpfCnpj)
        val userName = findViewById<EditText>(R.id.editUserName)
        val checkAction = findViewById<Switch>(R.id.switchAccess)
        val btnAccess = findViewById<Button>(R.id.btnConcludeAction)
        val selectCompanyOrPerson = findViewById<RadioGroup>(R.id.selectCompanyOrPerson)
        val personSelected = findViewById<RadioButton>(R.id.radioPerson)
        val companySelected = findViewById<RadioButton>(R.id.radioCompany)

        auth = firebaseConfig.getFirebaseAuth()
        verifyLoggedUser()

        checkAction.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectCompanyOrPerson.visibility = View.VISIBLE
                repeatPassword.visibility = View.VISIBLE
                userCpfCnpj.visibility = View.VISIBLE
                userName.visibility = View.VISIBLE
            } else {
                selectCompanyOrPerson.visibility = View.GONE
                repeatPassword.visibility = View.GONE
                userCpfCnpj.visibility = View.GONE
                userName.visibility = View.GONE
            }
        }
        selectCompanyOrPerson.setOnCheckedChangeListener { _, _ ->
            if (personSelected.isChecked) {
                userName.hint = "Nome completo"
                userCpfCnpj.visibility = View.GONE
                userType = "U"
            }
            if (companySelected.isChecked) {
                userName.hint = "Nome da empresa"
                userCpfCnpj.visibility = View.VISIBLE
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
                userCpfCnpj.hint = "CPF"
            } else {
                userName.hint = "Nome da empresa"
                userCpfCnpj.hint = "CNPJ"
            }
        }

        btnAccess.setOnClickListener {
            val email = userEmail?.text.toString()
            val name = userName?.text.toString()
            val passowrd = userPassword?.text.toString()
            val cpfCnpj = userCpfCnpj?.text.toString()

            if (!email.isNullOrEmpty() || !passowrd.isNullOrEmpty()) {
                //o ideal é que tenha um campo no banco que permite ver se é empresa ou
                //cliente, o que vai determinar qual View a pessoa vai ver
                if (checkAction.isChecked) {
                    auth?.createUserWithEmailAndPassword(email, passowrd)
                        ?.addOnCompleteListener { task ->
                            when {
                                task.isSuccessful -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Cadastro realizado com sucesso!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    if (userType == "C") {
                                        var idCompany: String = base64Custom.encodeToBase64(email)!!
                                        company.cId = idCompany
                                        company.cCompanyName = name
                                        company.cType = userType
                                        company.cEmail = email
                                        company.cDocument = cpfCnpj
                                        company.dDate = SimpleDateFormat().format(Date())
                                        companyService.saveCompany(company)

                                        startActivity(
                                            Intent(
                                                applicationContext,
                                                CompanyActivity::class.java
                                            )
                                        )
                                    } else {
                                        var idUser: String = base64Custom.encodeToBase64(email)!!
                                        client.cId = idUser
                                        client.cClientName = name
                                        client.cType = userType
                                        client.cEmail = email
                                        client.dDate = SimpleDateFormat().format(Date())

                                        clientService.saveClient(client)

                                        startActivity(
                                            Intent(
                                                applicationContext,
                                                UserActivity::class.java
                                            )
                                        )
                                    }
                                }
                                else -> {
                                    var exception = ""
                                    exception = try {
                                        throw task.exception!!
                                    } catch (e: FirebaseAuthWeakPasswordException) {
                                        "Por favor, escolha uma senha mais forte!"
                                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                                        "Por favor, digite um e-mail válido!"
                                    } catch (e: FirebaseAuthUserCollisionException) {
                                        "Esse usuário já está cadastrado."
                                    }
                                    Toast.makeText(
                                        applicationContext,
                                        exception,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                } else {
                    auth?.signInWithEmailAndPassword(email, passowrd)
                        ?.addOnCompleteListener { task ->
                            when {
                                task.isSuccessful -> {
                                    verifyUserType(email)
                                }

                                else -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "E-mail e/ou senha incorretos!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Preencha os campos corretamente!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun verifyUserType(userEmail: String) {
        progress.visibility = View.VISIBLE
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

                if (userPath == null && companyPath == null)
                    container?.visibility = View.VISIBLE
                progress.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun verifyLoggedUser() {
        if (firebaseConfig.getFirebaseAuth().currentUser != null) {
            verifyUserType(firebaseConfig.getFirebaseAuth().currentUser!!.email!!)
        }
    }
}