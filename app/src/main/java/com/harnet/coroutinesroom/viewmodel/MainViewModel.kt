package com.harnet.coroutinesroom.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.harnet.coroutinesroom.model.LoginState
import com.harnet.coroutinesroom.model.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    val db by lazy { UserDatabase(getApplication()).userDao() }

    val userDeleted = MutableLiveData<Boolean>()
    val signout = MutableLiveData<Boolean>()

    fun onSignout() {
        LoginState.logout()
        signout.value = true
    }

    fun onDeleteUser() {
        coroutineScope.launch {
            val user = LoginState.user
            user?.let { db.deleteUser(it.id) }

            LoginState.logout()
            withContext(Dispatchers.Main) {
                userDeleted.value = false
            }
        }
    }

}