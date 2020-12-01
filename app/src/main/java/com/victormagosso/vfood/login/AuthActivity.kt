package com.victormagosso.vfood.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.google.firebase.auth.*
import com.victormagosso.vfood.R
import com.victormagosso.vfood.activity.HomeActivity

class AuthActivity : AppCompatActivity() {
    private val auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userEmail = findViewById<EditText>(R.id.editUserLogin)
        val userPassword = findViewById<EditText>(R.id.editUserRepeatPassword)
        val checkAction = findViewById<Switch>(R.id.switchAccess)
        val btnAccess = findViewById<Button>(R.id.btnConcludeAction)
        val selectCompanyOrPerson = findViewById<RadioGroup>(R.id.selectCompanyOrPerson)
        val personSelected = findViewById<RadioButton>(R.id.radioPerson)
        val companySelected = findViewById<RadioButton>(R.id.radioCompany)
        val repeatPassword = findViewById<EditText>(R.id.editUserRepeatPassword)
        val cpfCnpj = findViewById<EditText>(R.id.editCpfCnpj)
        val userName = findViewById<EditText>(R.id.editUserName)

        btnAccess.setOnClickListener {
            val email = userEmail.text.toString()
            val passowrd = userPassword.text.toString()

            if (email.isNotEmpty() || passowrd.isNotEmpty()) {
                //verificação switch
                if (checkAction.isChecked) {

                    selectCompanyOrPerson.visibility = View.VISIBLE
                    repeatPassword.visibility = View.VISIBLE
                    cpfCnpj.visibility = View.VISIBLE
                    userName.visibility = View.VISIBLE
                    if (personSelected.isChecked) {
                        userName.hint = "Nome completo"
                        cpfCnpj.hint = "CPF"
                    } else {
                        userName.hint = "Nome da empresa"
                        cpfCnpj.hint = "CNPJ"
                    }

                    auth?.createUserWithEmailAndPassword(email, passowrd)
                        ?.addOnCompleteListener { task ->
                            when {
                                task.isSuccessful -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Cadastro realizado com sucesso!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    openMainScreen()
                                }
                                else -> {
                                    var exception = try {
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
        startActivity(Intent(applicationContext, HomeActivity::class.java))
    }

    private fun isLogged() {
        var currentUser: FirebaseUser? = auth?.currentUser
        if (currentUser != null) {
            openMainScreen()
        }
    }
}