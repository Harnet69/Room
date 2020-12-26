package com.harnet.coroutinesroom.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.harnet.coroutinesroom.model.LoginState
import com.harnet.coroutinesroom.model.User
import com.harnet.coroutinesroom.model.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupViewModel(application: Application) : AndroidViewModel(application) {
    // create scope of a coroutine
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    //initialize a database(lazy means it uses only we need to)
    val db by lazy { UserDatabase(getApplication()).userDao() }

    val mSignupComplete = MutableLiveData<Boolean>()
    val mError = MutableLiveData<String>()

    fun signup(username: String, password: String, info: String) {
        // launch the coroutine where we access to a database
        coroutineScope.launch {
            //check if user already exists
            val existingUser = db.getUser(username)
            if (existingUser != null) {
                //! for updating UI we should be in Main dispatcher
                withContext(Dispatchers.Main) {
                    mError.value = "User already exists"
                }
            } else {
                // create an object with password as a hashcode
                val user = User(username, password.hashCode(), info)
                // insert a user to database and get its generated id
                val userId = db.insertUser(user)
                user.id = userId
                // update a LoginState
                LoginState.login(user)
                //! for updating UI we should be in Main dispatcher
                withContext(Dispatchers.Main){
                    mSignupComplete.value = true
                }
            }
        }
    }
}