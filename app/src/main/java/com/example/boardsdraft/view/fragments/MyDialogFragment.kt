package com.example.boardsdraft.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.boardsDraft.databinding.FragmentMyDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDialogFragment(
    private val displayMessage: String,
    private val choice : String,
    private val clickListener: OnItemClickListener
) : DialogFragment(){

    private var _binding: FragmentMyDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentMyDialogBinding.bind(view)

        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        binding.apply {
            message.text = displayMessage
            choiceButton.text = choice
        }
        binding.dfCancelButton.setOnClickListener{
            dismiss()
        }
        binding.choiceButton.setOnClickListener {
            clickListener.result("YES")
            dismiss()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    interface OnItemClickListener{
        fun result(choice:String)
    }


}