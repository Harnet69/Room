package com.harnet.coroutinesroom.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.harnet.coroutinesroom.R
import com.harnet.coroutinesroom.viewmodel.SignupViewModel
import kotlinx.android.synthetic.main.fragment_signup.*

class SignUpFragment : Fragment() {

    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signupBtn.setOnClickListener { onSignup(it) }
        gotoLoginBtn.setOnClickListener { onGotoLogin(it) }

        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.mSignupComplete.observe(viewLifecycleOwner, Observer { isComplete ->
            Toast.makeText(activity, "Sign up is complete", Toast.LENGTH_SHORT).show()

            val action = SignUpFragmentDirections.actionGoToMainFromSignup()
            Navigation.findNavController(signupUsername).navigate(action)
        })

        viewModel.mError.observe(viewLifecycleOwner, Observer { error ->
            Toast.makeText(activity, "Error $error", Toast.LENGTH_SHORT).show()
        })
    }

    private fun onSignup(v: View){
        val username = signupUsername.text.toString()
        val password = signupPassword.text.toString()
        val info = otherInfo.text.toString()

        if(username.isNullOrEmpty() || password.isNullOrEmpty() || info.isNullOrEmpty()){
            Toast.makeText(activity, "Fill all fields", Toast.LENGTH_SHORT).show()
        }else{
            viewModel.signup(username, password, info)
        }
    }

    private fun onGotoLogin(v: View) {
        val action = SignUpFragmentDirections.actionGoToLoginFromSignup()
        Navigation.findNavController(v).navigate(action)
    }


}
