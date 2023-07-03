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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentEditBoardBinding
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.view.viewModel.EditBoardsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import kotlin.math.pow

@AndroidEntryPoint
class EditBoardFragment : Fragment() {

    private var _binding: FragmentEditBoardBinding? = null
    private val binding get()= _binding!!
    private val viewModel: EditBoardsViewModel by viewModels()
    private lateinit var board:Project
    private var isImagePickerOpen = false
    private var compressedImageData :ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getBoardImage(requireArguments().getInt("projectID"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentEditBoardBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditBoardBinding.bind(view)

        binding.apply {
            oldBoardName.setText(requireArguments().getString("projectName"))

            saveButton.setOnClickListener {
                if(validate()){
                    board.projectName = oldBoardName.text.toString().trim()
                    board.image =compressedImageData
                    viewModel.updateBoard(board)
                    parentFragmentManager.popBackStack()
                    viewModel.updateTasks(board.projectID,board.projectName)
                }
            }
            boardImage.setOnClickListener {
                if (!isImagePickerOpen) {
                    isImagePickerOpen = true

                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    galleryLauncher.launch(galleryIntent)
                }
            }

            oldBoardName.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus){
                    oldBoardNameLayout.error = null
                }
            }
        }
        viewModel.board.observe(viewLifecycleOwner, Observer {
            board = it
            it.image?.let { image->
                binding.boardImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.size))
            }
        })
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        isImagePickerOpen = false
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            binding.boardImage.setImageURI(selectedImageUri)
            selectedImageUri?.let { uri ->
                compressedImageData = compressImage(uri, 2000 * 1024)
            }
        }
    }

    private fun validate(): Boolean{

        return if(binding.oldBoardName.text.isNullOrBlank()){
            binding.oldBoardNameLayout.error = "Board Name cannot Be Empty"
            false
        } else{
            true
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
        _binding= null
        requireActivity().findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar).title = board.projectName

    }


}