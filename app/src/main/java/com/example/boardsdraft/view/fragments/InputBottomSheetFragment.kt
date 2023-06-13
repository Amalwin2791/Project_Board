package com.example.boardsdraft.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.boardsDraft.databinding.FragmentInputBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputBottomSheetFragment(
    private val message: String,
    private val choice: String,
    private val customHint: String,
    private val clickListener: OnItemClickListener
) : BottomSheetDialogFragment() {

    private var _binding: FragmentInputBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentInputBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInputBottomSheetBinding.bind(view)

        val window = dialog?.window

        window?.apply {
            setDimAmount(0f)

            setBackgroundDrawableResource(android.R.color.transparent)
        }

        binding.apply {
            editableMessage.text = message
            edfChoiceButton.text = choice
            dialogEmail.hint = customHint
            dialogEmail.hintTextColors.defaultColor

        }
        binding.edfChoiceButton.setOnClickListener {
            clickListener.action(binding.dialogEmail.text.toString().trim())
            dismiss()
        }
        binding.edfCancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    interface OnItemClickListener{
        fun action(value:String)
    }




}