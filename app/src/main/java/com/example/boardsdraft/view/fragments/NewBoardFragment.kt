package com.example.boardsdraft.view.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentNewBoardBinding
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.view.viewModel.BoardsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import kotlin.math.pow

@AndroidEntryPoint
class NewBoardFragment : Fragment() {

    private var _binding : FragmentNewBoardBinding? = null
    private val binding get() = _binding!!
    private val viewModel : BoardsViewModel by viewModels()
    private var compressedImageData :ByteArray? = null
    private var projectID :Int =0
    private var isImagePickerOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding =  FragmentNewBoardBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewBoardBinding.bind(view)
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.title ="Create A New Board"
        toolbar.menu.clear()

        viewModel.lastProjectID.observe(viewLifecycleOwner, Observer {
            projectID = viewModel.lastProjectID.value?.plus(1) ?: 1
        })

        binding.createButton.setOnClickListener {
            if (binding.newBoardName.text.isNullOrEmpty()) {
                binding.newBoardName.error = "Board Name Cannot Be Empty"
            } else {
                val projectName = binding.newBoardName.text.toString()
                val projectCode = generateRandomString()
                val board = Project(projectID=projectID, projectName = projectName, image = compressedImageData,
                    createdBy =viewModel.getCurrentUserName()!!, projectCode = projectCode)
                viewModel.insertBoard(board)
                viewModel.insertUserProjectCrossRef(board.projectID)
                parentFragmentManager.popBackStack()
            }
        }

        binding.cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()

        }

        binding.boardImage.setOnClickListener {
            if (!isImagePickerOpen) {
                isImagePickerOpen = true

                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryLauncher.launch(galleryIntent)
            }
        }

    }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        isImagePickerOpen = false
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            binding.boardImage.setImageURI(selectedImageUri)
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


    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().findViewById<Toolbar>(R.id.toolbar).inflateMenu(R.menu.top_app_bar)
        requireActivity().findViewById<Toolbar>(R.id.toolbar).title = "Boards"
        _binding= null

    }
    private fun generateRandomString(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z')
        return (1..5)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }


}