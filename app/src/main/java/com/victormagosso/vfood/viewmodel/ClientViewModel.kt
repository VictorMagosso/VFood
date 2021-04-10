package com.victormagosso.vfood.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.victormagosso.vfood.model.client.Client
import com.victormagosso.vfood.repository.ClientRepository

import com.victormagosso.vfood.repository.CompanyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClientViewModel(private val repository: ClientRepository): ViewModel() {
    var statusMsg = MutableLiveData<String?>()
    var emptyFieldMsg = MutableLiveData<String?>()
    var userLiveData: MutableLiveData<Client> = repository.userLiveData
    var isFieldEmpty = MutableLiveData<Boolean>()

    var isSuccessCreatingLogin: MutableLiveData<Boolean?> = repository.isSuccessCreatingLogin
    var isSuccessPersistingData: MutableLiveData<Boolean?> = repository.isSuccessPersistingData
    var isSuccessLogging: MutableLiveData<Boolean?> = repository.isSuccessLogging
    var isSuccessSendingEmail: MutableLiveData<Boolean?> = repository.isSuccessSendingEmail

    var errorMsgCreatingAccount: MutableLiveData<String?> = repository.errorMsgCreatingAccount
    var errorMsgLogin: MutableLiveData<String?> = repository.errorMsgLogin

    var errorSendingEmail: MutableLiveData<String?> = repository.errorSendingEmail

    fun createUserWithEmailAndPassword(email: String, password: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                repository.createUserWithEmailAndPassword(email, password)
            } catch (e: FirebaseAuthWeakPasswordException) {
                Log.d("erro VM: ", e.message.toString())
            }
        }
    }

    fun signInUsingEmailAndPassword(email: String, password: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                repository.signInUsingEmailAndPassword(email, password)
            } catch (e: Exception) {
                Log.d("erro VM: ", e.message.toString())
            }
        }
    }

    fun persistUser(client: Client) {
        CoroutineScope(Dispatchers.Main).launch {
            if (client.cClientName?.isNotEmpty()!!) {
                try {
                    repository.persistUser(client)
                } catch (e: Exception) {
                    statusMsg.value = e.message
                }
            } else {
                emptyFieldMsg.value = "Preencha todos os campos!"
                isFieldEmpty.value = true
            }
        }
    }

    fun getUserData(email: String, name: String): Client {
        return Client(name, email)
    }

    fun sendResetPasswordEmail(email: String) {
        CoroutineScope(Dispatchers.Main).launch {
            repository.sendEmailToResetPassword(email)
        }
    }

    fun getUser() {
        repository.getUser()
    }

    fun updateUser(name: String) {
        CoroutineScope(Dispatchers.Main).launch {
            repository.updateUser(name)
        }
    }

    fun logoff() {
        repository.logoff()
    }

    fun deleteAccount() {
        repository.deleteAccount()
    }
}
}