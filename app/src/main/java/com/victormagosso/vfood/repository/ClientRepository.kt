package com.victormagosso.vfood.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.client.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClientRepository {
    var firebaseConfig = FirebaseConfig()
    var userFirbaseData = UserFirebaseData()

    var gso: GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

    private var storageReference: StorageReference? = null

    var isSuccessCreatingLogin = MutableLiveData<Boolean?>()
    var isSuccessPersistingData = MutableLiveData<Boolean?>()
    var isSuccessLogging = MutableLiveData<Boolean?>()
    var isSuccessSendingEmail = MutableLiveData<Boolean?>()

    var errorMsgCreatingAccount = MutableLiveData<String?>()
    var errorMsgLogin = MutableLiveData<String?>()

    var errorSendingEmail = MutableLiveData<String?>()

    private val firebaseAuth: FirebaseAuth = FirebaseConfig().getFirebaseAuth()
    private val dbRef: DatabaseReference = FirebaseConfig().getFirebaseDatabase()
    var userLiveData = MutableLiveData<Client>()

    suspend fun createUserWithEmailAndPassword(email: String, password: String) {
        isSuccessCreatingLogin.value = false
        withContext(Dispatchers.Main) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> isSuccessCreatingLogin.value = task.isSuccessful
                        else -> {
                            errorMsgCreatingAccount.value = try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthWeakPasswordException) {
                                "Sua senha está muito fraca"
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                "Seu e-mail não é válido"
                            } catch (e: FirebaseAuthUserCollisionException) {
                                "Já existe um cadastro com esse e-mail"
                            }
                        }
                    }
                }
        }
    }

    suspend fun signInUsingEmailAndPassword(email: String, password: String) {
        isSuccessCreatingLogin.value = false
        withContext(Dispatchers.Main) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> isSuccessLogging.value = task.isSuccessful
                        else -> {
                            errorMsgLogin.value = try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                "Usuário ou senha incorretos"
                            } catch (e: FirebaseAuthInvalidUserException) {
                                "Usuário ou senha incorretos"
                            }
                        }
                    }
                }
        }
    }

    suspend fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        withContext(Dispatchers.Main) {

            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    isSuccessCreatingLogin.value = task.isSuccessful
                }
        }
    }


    suspend fun sendEmailToResetPassword(email: String) {
        withContext(Dispatchers.Main) {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> isSuccessSendingEmail.value = task.isSuccessful
                        else -> {
                            errorSendingEmail.value = "Erro ao enviar para o e-mail selecionado"
                        }
                    }
                }
        }
    }

    suspend fun persistUser(client: Client) {
        isSuccessPersistingData.value = false
        withContext(Dispatchers.Main) {
            dbRef.child("clients")
                .child(firebaseAuth.currentUser!!.uid)
                .setValue(client)
                .addOnCompleteListener { task ->
                    isSuccessPersistingData.value = task.isSuccessful
                }
        }
    }

    suspend fun updateUser(name: String) {
        var user = Client(name, firebaseAuth.currentUser!!.email!!)
        withContext(Dispatchers.Main) {
            dbRef.child("clients")
                .child(firebaseAuth.currentUser!!.uid)
                .setValue(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        isSuccessPersistingData.value = true
                    }
                }
        }
    }

    fun getUser() {
        var client = Client()
        dbRef.child("users")
            .child(firebaseAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    client = snapshot.getValue(Client::class.java)!!
                    userLiveData.value = client
                }

                override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
                }
            })
    }

    fun logoff() {
        firebaseAuth.signOut()
    }

    fun deleteAccount() {
        dbRef.child("users")
            .child(firebaseAuth.currentUser!!.uid)
            .removeValue()

        dbRef.child("transactions")
            .child(firebaseAuth.currentUser!!.uid)
            .removeValue()

        firebaseAuth.currentUser?.delete()
        logoff()
    }
}
