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

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    // create scope of a coroutine
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    //initialize a database(lazy means it uses only we need to)
    val db by lazy { UserDatabase(getApplication()).userDao() }

    val loginComplete = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun login(username: String, password: String) {
        coroutineScope.launch {
            val userFromDB = db.getUser(username)
            if(userFromDB != null && userFromDB.passwordHash == password.hashCode()){
                withContext(Dispatchers.Main){
                    LoginState.login(userFromDB)
                    loginComplete.value = true
                }
            }else{
                withContext(Dispatchers.Main){
                    error.value = "Wrong user or password"
                }
            }
        }
    }
}