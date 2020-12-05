package com.victormagosso.vfood.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.victormagosso.vfood.R
import com.victormagosso.vfood.activity.HomeActivity
import com.victormagosso.vfood.activity.company.CompanyActivity
import com.victormagosso.vfood.activity.user.UserActivity
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.Base64Custom
import com.victormagosso.vfood.helper.UserFirebase
import com.victormagosso.vfood.model.User
import com.victormagosso.vfood.service.UserService
import java.text.SimpleDateFormat
import java.util.*


class AuthActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var firebaseConfig = FirebaseConfig()
    var userFirebase = UserFirebase()
    var base64Custom = Base64Custom()
    val userService = UserService()
    var dashboard = Intent()
    var user = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_auth)

        var userType = "C"
        val userEmail = findViewById<EditText>(R.id.editUserLogin)
        val userPassword = findViewById<EditText>(R.id.editUserPassword)
        val repeatPassword = findViewById<EditText>(R.id.editUserRepeatPassword)
        val userCpfCnpj = findViewById<EditText>(R.id.editCpfCnpj)
        val userName = findViewById<EditText>(R.id.editUserName)
        val checkAction = findViewById<Switch>(R.id.switchAccess)
        val btnAccess = findViewById<Button>(R.id.btnConcludeAction)
        val selectCompanyOrPerson = findViewById<RadioGroup>(R.id.selectCompanyOrPerson)
        val personSelected = findViewById<RadioButton>(R.id.radioPerson)
        val companySelected = findViewById<RadioButton>(R.id.radioCompany)
        dashboard = Intent(applicationContext, CompanyActivity::class.java)

        auth = firebaseConfig.getFirebaseAuth()

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
                userCpfCnpj.hint = "CPF"
                userType = "U"
                dashboard = Intent(applicationContext, UserActivity::class.java)
            }
            if (companySelected.isChecked){
                userName.hint = "Nome da empresa"
                userCpfCnpj.hint = "CNPJ"
                userType = "C"
                dashboard = Intent(applicationContext, CompanyActivity::class.java)
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
                                    var idUser: String = base64Custom.encodeToBase64(email)!!
                                    user.cId = idUser
                                    user.cName = name
                                    user.cEmail = email
                                    user.dDoc = cpfCnpj
                                    user.dDate = SimpleDateFormat().format(Date())
                                    user.cType = userType
                                    userService.saveUser(user)
                                    openMainScreen()
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
                                    firebaseConfig.getUserRef().addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            user = snapshot.value as User //similar a getValue(User.class)
                                            userType = user.cType!!
                                            if (userType.toUpperCase() == "C") dashboard = Intent(applicationContext, CompanyActivity::class.java)
                                            if (userType.toUpperCase() == "U") dashboard = Intent(applicationContext, UserActivity::class.java)
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }
                                    })
                                    openMainScreen()
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

    private fun openMainScreen() {
       startActivity(dashboard)
    }

    private fun isLogged() {
        var currentUser: FirebaseUser? = auth?.currentUser
        if (currentUser != null) {
            openMainScreen()
        }
    }
}