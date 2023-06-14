package com.example.boardsdraft.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentAddBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBottomSheetFragment(
    private val clickListener: OnItemClickListener
) : BottomSheetDialogFragment(), InputBottomSheetFragment.OnItemClickListener {

    private var _binding: FragmentAddBottomSheetBinding?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddBottomSheetBinding.bind(view)

        val window = dialog?.window

        window?.apply {
            setDimAmount(0f)

            setBackgroundDrawableResource(android.R.color.transparent)
        }


        binding.addOptions.apply {
            val options = resources.getStringArray(R.array.add_click_options)
            adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,options)
             setOnItemClickListener { _, _, position, _ ->
                 if(position == 0){
                     InputBottomSheetFragment("Enter Board ID", "Join","Project Code",
                         this@AddBottomSheetFragment).show(parentFragmentManager,"BottomFrag")
                     dismiss()

                 }
                 else{
                     val transaction = parentFragmentManager.beginTransaction()
                     transaction.replace(R.id.boards_list_frame, NewBoardFragment())
                     transaction.addToBackStack(null)
                     transaction.commit()
                     dismiss()
                 }
             }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun action(value: String) {
        clickListener.joinBoard(value)
    }

    interface OnItemClickListener{
        fun joinBoard(value: String)
    }


}