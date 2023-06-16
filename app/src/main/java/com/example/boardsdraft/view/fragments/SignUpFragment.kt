package com.example.boardsdraft.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.boardsDraft.databinding.FragmentSignUpBinding
import com.example.boardsdraft.view.activities.HomeActivity
import com.example.boardsdraft.view.enums.LoginResults
import com.example.boardsdraft.view.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)
        binding.signUpButton.setOnClickListener {
            viewModel.signUp(binding.signUpName.text.toString().trim(),binding.signUpEmail.text.toString().trim(),
                binding.signUpPassword.text.toString().trim(),binding.signUpRetypePassword.text.toString().trim())
        }
        viewModel.signUpStatus.observe(viewLifecycleOwner) { signUpStatus ->
            when(signUpStatus){
                LoginResults.LOGIN_SUCCESSFUL ->{
                    startActivity(Intent(context, HomeActivity::class.java))
                    requireActivity().finish()
                }
                LoginResults.INVALID_EMAIL->{
                    binding.signUpEmailLayout.error = "Enter Valid Email"
                }
                LoginResults.FIELD_IS_NULL -> showNullFieldsError()
                LoginResults.PASSWORDS_DONT_MATCH -> binding.signUpRetypePasswordLayout.error = "Passwords Don't Match"
                LoginResults.PASSWORD_DONT_MEET_REQUIREMENT ->{
                    binding.signUpPasswordLayout.error = "Passwords Don't Meet Requirements(must have 8–20 chars,at least 1 uppercase,1 number,1 special char,1 lower case)"
                    binding.signUpRetypePasswordLayout.error = "Passwords Don't Meet Requirements"
                }
                LoginResults.USER_ALREADY_PRESENT -> {
                    binding.signUpName.text?.clear()
                    binding.signUpEmail.text?.clear()
                    binding.signUpPassword.text?.clear()
                    binding.signUpRetypePassword.text?.clear()
                    Toast.makeText(context,"User Already Present, Sign In",Toast.LENGTH_SHORT).show()
                }
                else->{}

            }

        }

    }

    private fun showNullFieldsError(){
        if (binding.signUpName.text.isNullOrEmpty()){
            binding.signUpNameLayout.error = "Name cannot Be Empty"
        }
        if (binding.signUpEmail.text.isNullOrEmpty()){
            binding.signUpEmailLayout.error = "Email Cannot Be Empty"
        }
        if (binding.signUpPassword.text.isNullOrEmpty()){
            binding.signUpPasswordLayout.error = "Password Cannot be Empty"
        }
        if (binding.signUpRetypePassword.text.isNullOrEmpty()){
            binding.signUpRetypePasswordLayout.error = "Retype Your Password"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null

    }



}