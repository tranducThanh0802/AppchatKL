package com.example.appchatkl.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.appchatkl.R
import com.example.appchatkl.CommomFunction
import com.example.appchatkl.data.db.AppDatabase
import com.example.appchatkl.data.db.ChatDBViewModel
import com.example.appchatkl.data.db.data.Save
import com.example.appchatkl.databinding.LoginFragmentBinding
import com.example.appchatkl.ui.content.friend.friend.FriendFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    val TAG = "LoginFragment"
    private lateinit var controller: NavController
    lateinit  var chatDB: AppDatabase
     private val viewModel1: ChatDBViewModel by activityViewModels()

    companion object {
        fun newInstance() = FriendFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: LoginFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.login_fragment, container, false
        )
        val view: View = binding.root
        binding.lifecycleOwner = this
        chatDB = AppDatabase.getDatabase(view.context)
        val loginViewModel: LoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.login = loginViewModel
        controller = findNavController()
        binding.txtRegister.setOnClickListener {
            controller.navigate(R.id.registerFragment2)
        }
        binding.btnDn.setOnClickListener {
            loginViewModel.onLogin()

        }
        loginViewModel.isCheck.observe(viewLifecycleOwner, {
            Log.d(TAG, "onCreateView: " + it)
            if (it) {
                controller.navigate(R.id.bottomFragment)
            }
        })
        return view
    }


    override fun onStart() {
        super.onStart()
        if (CommomFunction.currentUser != null) {
            val uid = CommomFunction.currentUser.uid
            viewModel1.insertSave(Save(uid))
            controller.navigate(R.id.bottomFragment)
        } else {
            viewModel1.loadSave().forEach {
                if (it.id!=("null")) {
                    controller.navigate(R.id.bottomFragment)
                }
            }
        }

    }

}