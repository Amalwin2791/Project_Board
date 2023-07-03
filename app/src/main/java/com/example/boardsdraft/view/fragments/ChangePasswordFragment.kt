package com.example.boardsdraft.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentChangePasswordBinding
import com.example.boardsdraft.view.viewModel.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCurrentUser()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChangePasswordBinding.bind(view)

        val toolBar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolBar.apply {
            title = "Change Password"
        }

        binding.apply {

            changePasswordButton.setOnClickListener{
                println(validatePassword())
                if(validatePassword()){
                    println("done")
                    viewModel.user.observe(viewLifecycleOwner, Observer {
                        viewModel.currentUser.password = newPassword.text.toString().trim()
                        viewModel.updateUser(viewModel.currentUser)
                        Toast.makeText(requireContext(),"Password Updated",Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    })
                }

            }
            oldPassword.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    oldPasswordLayout.error = null
                }
            }

            newPassword.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus){
                    newPasswordLayout.error= null
                }
            }
            newRetypePassword.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus){
                    newRetypePasswordLayout.error= null
                }
            }

        }
    }

    private fun validatePassword(): Boolean {
        binding.apply {
            val currentPassword = viewModel.getCurrentUserPassword()
            val passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$".toRegex()

            when {
                !passwordRegex.matches(newPassword.text.toString().trim()) -> {
                    newPasswordLayout.error = "Password doesn't meet the requirements"
                }
                oldPassword.text.isNullOrBlank() -> {
                    oldPasswordLayout.error = "Old Password cannot be empty"
                }
                oldPassword.text.toString().trim() != currentPassword -> {
                    oldPasswordLayout.error = "Enter the correct Old Password"
                }
                newPassword.text.toString().trim() == currentPassword -> {
                    newPasswordLayout.error = "The new password cannot be the same as the old password"
                }
                newPassword.text.toString().trim() != newRetypePassword.text.toString().trim() -> {
                    newRetypePasswordLayout.error = "New passwords don't match"
                }
                newPassword.text.isNullOrBlank() -> {
                    newPasswordLayout.error = "New Password cannot be empty"
                }
                newRetypePassword.text.isNullOrBlank() -> {
                    newRetypePasswordLayout.error = "Please retype your new password"
                }
                else -> {
                    return true
                }
            }
            return false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
        requireActivity().findViewById<Toolbar>(R.id.toolbar).title = "Profile"

    }


}