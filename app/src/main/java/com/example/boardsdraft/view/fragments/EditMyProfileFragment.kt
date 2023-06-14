package com.example.boardsdraft.view.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentEditMyProfileBinding
import com.example.boardsdraft.view.viewModel.MyProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import kotlin.math.pow

@AndroidEntryPoint
class EditMyProfileFragment : Fragment() {

    private var _binding: FragmentEditMyProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyProfileViewModel by viewModels()

    private var isImagePickerOpen = false
    private var compressedImageData :ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCurrentUser()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditMyProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditMyProfileBinding.bind(view)

        val toolbar: androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.profile_toolbar)
        toolbar.apply {
            title = "Edit Profile"
            menu.clear()
        }

        viewModel.user.observe(viewLifecycleOwner, Observer {
            binding.apply {
                editProfileName.setText(viewModel.getCurrentUserName())
                editProfileEmail.setText(viewModel.getCurrentUserEmailID())

                if(viewModel.getCurrentUserImage() != null){
                    val image = viewModel.getCurrentUserImage()
                    editProfileImage.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image!!.size))
                }

                if(viewModel.getCurrentUserDepartment() != null){
                    editSelectDepartment.setText(viewModel.getCurrentUserDepartment(),false)
                }

                if(viewModel.getCurrentUserDesignation() != null){
                    editSelectDesignation.setText(viewModel.getCurrentUserDesignation(),false)
                }


                editProfileImage.setOnClickListener {
                    if (!isImagePickerOpen) {
                        isImagePickerOpen = true

                        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        galleryLauncher.launch(galleryIntent)
                    }
                }



                saveChangesToProfile.setOnClickListener{
                    viewModel.currentUser.apply {
                        if(editProfileName.text.isNullOrEmpty()){
                            editProfileNameLayout.error = "Name Cannot Be Empty"
                        }
                        else{
                            userName = editProfileName.text.toString().trim()
                        }

                        if(editProfileEmail.text.isNullOrEmpty()){
                            editProfileEmailLayout.error = "Email Cannot Be Empty"
                        }
                        else{
                            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                if(viewModel.doesEmailExist(editProfileEmail.text.toString().trim())){
                                    editProfileEmailLayout.error = "Email Already Present"
                                }
                                else{
                                    email = editProfileEmail.text.toString().trim()
                                }
                            }
                        }

                        if(!editSelectDepartment.text.isNullOrEmpty()){
                            department = editSelectDepartment.text.toString()
                        }
                        if(!editSelectDesignation.text.isNullOrEmpty()){
                            designation = editSelectDesignation.text.toString()
                        }
                        if (compressedImageData!=null){
                            image = compressedImageData
                        }
                    }

                    viewModel.updateUser(viewModel.currentUser)
                    parentFragmentManager.popBackStack()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        val designations = resources.getStringArray(R.array.designations)
        val designationArrayAdapter = ArrayAdapter(requireContext(),R.layout.item_priority_color,designations)
        binding.editSelectDesignation.apply {
            setAdapter(designationArrayAdapter)
            keyListener = null
        }

        val departments = resources.getStringArray(R.array.departments)
        val departmentsArrayAdapter = ArrayAdapter(requireContext(),R.layout.item_priority_color,departments)
        binding.editSelectDepartment.apply {
            setAdapter(departmentsArrayAdapter)
            keyListener = null
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        isImagePickerOpen = false
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            binding.editProfileImage.setImageURI(selectedImageUri)
            selectedImageUri?.let { uri ->
                compressedImageData = compressImage(uri, 1000 * 1024)
            }
        }
    }

    private fun compressImage(imageUri: Uri, maxSizeInBytes: Long): ByteArray? {
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()

        var scale = 1
        while ((options.outWidth * options.outHeight * (1 / scale.toDouble().pow(2.0))) > maxSizeInBytes) {
            scale++
        }

        val outputStream = ByteArrayOutputStream()
        val decodeOptions = BitmapFactory.Options()
        decodeOptions.inSampleSize = scale
        val compressedBitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(imageUri), null, decodeOptions)
        compressedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        compressedBitmap?.recycle()
        return outputStream.toByteArray()
    }


}