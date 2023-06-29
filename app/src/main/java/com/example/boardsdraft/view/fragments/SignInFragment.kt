package com.example.boardsdraft.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.boardsDraft.databinding.FragmentSignInBinding
import com.example.boardsdraft.view.SessionManager
import com.example.boardsdraft.view.activities.HomeActivity
import com.example.boardsdraft.view.enums.LoginResults
import com.example.boardsdraft.view.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment(),InputBottomSheetFragment.OnItemClickListener {

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
        binding.forgotPassword.setOnClickListener {

            InputBottomSheetFragment("Send New Password To Email ID: ", "Send","Email ID",
                this@SignInFragment).show(parentFragmentManager,"BottomFrag")

        }
        binding.apply {
            signInEmail.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    signInEmailLayout.error = null
                }
            }
            signInPassword.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    signInPasswordLayout.error = null
                }
            }
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
                    Toast.makeText(requireContext(), "Sign Up To login", Toast.LENGTH_SHORT).show()
                }
                LoginResults.INVALID_PASSWORD -> {
                    binding.signInPasswordLayout.error = "Enter Valid Password"
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


    override fun action(value: String) {
        viewModel.getUserByEmailID(value)

        viewModel.user.observe(viewLifecycleOwner, Observer {
            if(it != null){
                val newPassword = generatePassword()
                viewModel.user.value!!.password = newPassword
                viewModel.updateUser(viewModel.user.value!!)
                val emailIntent= Intent(Intent.ACTION_SENDTO)
                emailIntent.apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL,value)
                    putExtra(Intent.EXTRA_SUBJECT,"New Password")
                    putExtra(
                        Intent.EXTRA_TEXT,"Your New Password is $newPassword, Use the Entered Email ID To Login.")
                    if (emailIntent.resolveActivity(requireActivity().packageManager) != null) {
                        requireActivity().startActivity(Intent.createChooser(emailIntent, "Choose An App"))
                    }
                }




            }
            else{
                Toast.makeText(requireContext(), "Enter The Correct Email ID", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun generatePassword(): String {

        val characters = "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "!@#$%^&*()-_=+{}[]:;'<>,./?"

        val random = Random()
        val password = StringBuilder()

        for (i in 1..9) {
            val characterIndex = random.nextInt(characters.length)
            password.append(characters[characterIndex])
        }

        return password.toString()
    }


}