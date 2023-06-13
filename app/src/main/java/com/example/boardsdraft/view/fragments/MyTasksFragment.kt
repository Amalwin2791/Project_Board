package com.example.boardsdraft.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentMyTasksBinding

class MyTasksFragment : Fragment() {

    private var _binding : FragmentMyTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentMyTasksBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyTasksBinding.bind(view)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.title= "My Tasks"
        toolbar.menu.clear()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null

    }

}