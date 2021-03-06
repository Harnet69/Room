package com.harnet.coroutinesroom.view


import android.app.AlertDialog
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
import com.harnet.coroutinesroom.model.LoginState
import com.harnet.coroutinesroom.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usernameTV.text = LoginState.user?.username
        signoutBtn.setOnClickListener { onSignOut() }
        deleteUserBtn.setOnClickListener { onDelete() }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.signout.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, "Logged out", Toast.LENGTH_SHORT).show()
            goToLogin()
        })

        viewModel.userDeleted.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, "User deleted", Toast.LENGTH_SHORT).show()
            goToSignup()
        })
    }

    private fun onDelete() {
        //the checking prevent from crash if app is in background
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Delete user")
                .setMessage("Are you sure you want to delete the user")
                .setPositiveButton("Yes") { p0, p1 -> viewModel.onDeleteUser() }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }

    private fun onSignOut() {
        viewModel.onSignout()
    }

    private fun goToLogin() {
        val action = MainFragmentDirections.actionGoToLoginFromMain()
        Navigation.findNavController(signoutBtn).navigate(action)
    }

    private fun goToSignup() {
        val action = MainFragmentDirections.actionGoToSignupFromMain()
        Navigation.findNavController(signoutBtn).navigate(action)
    }

}