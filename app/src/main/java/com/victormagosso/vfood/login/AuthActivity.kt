package com.victormagosso.vfood.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.CubeGrid
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
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
import kotlinx.android.synthetic.main.activity_auth.*
import java.text.SimpleDateFormat
import java.util.*


class AuthActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 1000
    var auth: FirebaseAuth? = null
    var firebaseConfig = FirebaseConfig()
    var base64Custom = Base64Custom()
    private val companyRepository = CompanyRepository()
    private val clientRepository = ClientRepository()
    var company = Company()
    var client = Client()
    var userType = "C"

    var gso: GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

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
        createRequest()

        btnSignInWithGoogle?.setOnClickListener {
            userPassword.visibility = View.GONE
            repeatPassword.visibility = View.GONE
            userEmail.visibility = View.GONE
            signIn()
        }

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

        btnAccess.setOnClickListener {
            val email = userEmail?.text.toString()
            val name = userName?.text.toString()
            val passowrd = userPassword?.text.toString()
            val repeatPassowrd = repeatPassword?.text.toString()
            val cpfCnpj = userCpfCnpj?.text.toString()
            val tel = userTel?.text.toString()

            if (userPassword.visibility == View.VISIBLE) {
                if (!email.isNullOrEmpty() ||
                    !passowrd.isNullOrEmpty() ||
                    repeatPassowrd != passowrd
                ) {
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
                                            var idCompany: String =
                                                base64Custom.encodeToBase64(email)!!
                                            company.cId = idCompany
                                            company.cCompanyName = name
                                            company.cType = userType
                                            company.cEmail = email
                                            company.cDocument = cpfCnpj
                                            company.dDate = SimpleDateFormat().format(Date())
                                            companyRepository.saveCompany(company)

                                            startActivity(
                                                Intent(
                                                    applicationContext,
                                                    CompanyActivity::class.java
                                                )
                                            )
                                        } else {
                                            var idUser: String =
                                                base64Custom.encodeToBase64(email)!!
                                            client.cId = idUser
                                            client.cClientName = name
                                            client.cType = userType
                                            client.cEmail = email
                                            client.cTelephone = tel
                                            client.dDate = SimpleDateFormat().format(Date())

                                            clientRepository.saveClient(client)

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
    }

    override fun onStart() {
        super.onStart()

//        var user: FirebaseUser = auth?.currentUser!!
//        if(user!=null) {
//            Toast.makeText(this,"Tem alguem logad", Toast.LENGTH_SHORT).show()
//        }
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
                    progress.visibility = View.GONE
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
            progress.visibility = View.GONE
        }
    }

    private fun createRequest() {
        //Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //Build a GoogleSignInClient with the options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    var email = firebaseConfig.getFirebaseAuth().currentUser?.email!!
                    Toast.makeText(
                        applicationContext,
                        "Autenticação com o google: $email",
                        Toast.LENGTH_LONG
                    ).show()
                    btnAccess.setOnClickListener {

                        val name = userName?.text.toString()
                        val cpfCnpj = userCpfCnpj?.text.toString()
                        val tel = userTel?.text.toString()

                        if (userType == "C") {
                            if (!userName.toString().isNullOrEmpty() ||
                                !userCpfCnpj.toString().isNullOrEmpty()
                            ) {
                                var idCompany: String =
                                    base64Custom.encodeToBase64(email)!!
                                company.cId = idCompany
                                company.cCompanyName = name
                                company.cType = userType
                                company.cEmail = email
                                company.cDocument = cpfCnpj
                                company.dDate = SimpleDateFormat().format(Date())
                                companyRepository.saveCompany(company)

                                startActivity(
                                    Intent(
                                        applicationContext,
                                        CompanyActivity::class.java
                                    )
                                )
                                Toast.makeText(
                                    applicationContext,
                                    "Bem-vindo ${name.takeWhile { x -> !x.isWhitespace() }} !",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            if (!userName.toString().isNullOrEmpty() ||
                                !userTel.toString().isNullOrEmpty()
                            ) {
                                var idUser: String =
                                    base64Custom.encodeToBase64(email)!!
                                client.cId = idUser
                                client.cClientName = name
                                client.cType = userType
                                client.cEmail = email
                                client.cTelephone = tel
                                client.dDate = SimpleDateFormat().format(Date())

                                clientRepository.saveClient(client)

                                startActivity(
                                    Intent(
                                        applicationContext,
                                        UserActivity::class.java
                                    )
                                )
                                Toast.makeText(
                                    applicationContext,
                                    "Bem-vindo ${name.takeWhile { x -> !x.isWhitespace() }} !",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Snackbar.make(View(this), "Authentication Failed.", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
    }

}