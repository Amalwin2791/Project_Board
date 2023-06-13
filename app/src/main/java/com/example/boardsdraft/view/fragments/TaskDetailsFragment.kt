package com.example.boardsdraft.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentTaskDetailsBinding


class TaskDetailsFragment : Fragment() {

    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTaskDetailsBinding.bind(view)



        }

    override fun onResume() {
        super.onResume()

        val colors = resources.getStringArray(R.array.priority_colors)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.item_priority_color,colors)
        binding.selectPriorityColor.setAdapter(arrayAdapter)



    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}