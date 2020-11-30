package com.victormagosso.vfood.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.victormagosso.vfood.R
import com.victormagosso.vfood.activity.HomeActivity
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    private val auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userName = findViewById<EditText>(R.id.editUserLogin)
        val userPassword = findViewById<EditText>(R.id.editUserPassword)
        val checkAction = findViewById<Switch>(R.id.switchAccess)
        val btnAccess = findViewById<Button>(R.id.btnConcludeAction)

        btnAccess.setOnClickListener {
            val email = userName.text.toString()
            val passowrd = userPassword.text.toString()

            if (email.isNotEmpty() || passowrd.isNotEmpty()) {
                //verificação switch
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