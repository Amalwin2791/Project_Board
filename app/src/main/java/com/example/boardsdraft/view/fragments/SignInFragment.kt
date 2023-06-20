package com.example.boardsdraft.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.boardsDraft.databinding.FragmentSignInBinding
import com.example.boardsdraft.view.SessionManager
import com.example.boardsdraft.view.activities.HomeActivity
import com.example.boardsdraft.view.enums.LoginResults
import com.example.boardsdraft.view.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var sharedPreferences : SessionManager

    private val viewModel : LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignInBinding.bind(view)


        if (sharedPreferences.isLoggedIn()) {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            requireActivity().finish()

        }

        binding.signInButton.setOnClickListener {
            viewModel.signIn(
                binding.signInEmail.text.toString().trim(),
                binding.signInPassword.text.toString().trim()
            )
        }

        viewModel.signInStatus.observe(viewLifecycleOwner) { signInStatus ->
            when (signInStatus) {
                LoginResults.INVALID_EMAIL->{
                    binding.signInEmailLayout.error="Enter Valid Email"
                }
                LoginResults.LOGIN_SUCCESSFUL -> {
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    requireActivity().finish()
                }
                LoginResults.FIELD_IS_NULL -> showNullFieldsError()
                LoginResults.USER_NOT_PRESENT -> {
                    binding.signInEmail.setText("")
                    binding.signInPassword.setText("")
                    Toast.makeText(requireContext(), "Sign Up To login", Toast.LENGTH_SHORT).show()
                }
                LoginResults.INVALID_PASSWORD -> {
                    binding.signInPasswordLayout.error = "Enter Valid Password"
                    binding.signInPassword.setText("")
                }
                else -> {}
            }
        }
    }


    private fun showNullFieldsError() {
        if(binding.signInEmail.text.isNullOrBlank()){
            binding.signInEmailLayout.error = "Email ID cannot Be Empty"
        }
        if(binding.signInPassword.text.isNullOrBlank()){
            binding.signInPasswordLayout.error = "Password cannot Be Empty"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null

    }


}