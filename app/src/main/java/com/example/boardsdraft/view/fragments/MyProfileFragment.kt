package com.example.boardsdraft.view.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentMyProfileBinding
import com.example.boardsdraft.view.activities.LoginActivity
import com.example.boardsdraft.view.activities.ManageProfileActivity
import com.example.boardsdraft.view.viewModel.MyProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment: Fragment() {

    private var _binding : FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCurrentUser()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentMyProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyProfileBinding.bind(view)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.title= "Profile"
        toolbar.menu.clear()

        viewModel.user.observe(viewLifecycleOwner, Observer {
            if(viewModel.getCurrentUserImage() != null){
                val image = viewModel.getCurrentUserImage()
                binding.profileImage.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image!!.size))
            }
            binding.profileName.apply {
                text = viewModel.getCurrentUserName()
            }


        })

        binding.logOut.setOnClickListener {
            viewModel.clearSession()
            requireContext().startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
        binding.viewMyProfile.setOnClickListener {
            startActivity(Intent(requireContext(),ManageProfileActivity::class.java)
                .putExtra("currentUserID",viewModel.getCurrentUserID()))
        }

        binding.changeMyPassword.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.home,ChangePasswordFragment())
                .addToBackStack(null)
                .commit()
        }


    }

    override fun onResume() {
        super.onResume()
        viewModel.getCurrentUser()
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null

    }


}