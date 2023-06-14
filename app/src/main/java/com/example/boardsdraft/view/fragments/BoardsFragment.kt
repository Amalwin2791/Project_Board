package com.example.boardsdraft.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentBoardsBinding
import com.example.boardsdraft.view.activities.TasksActivity
import com.example.boardsdraft.view.adapter.BoardsAdapter
import com.example.boardsdraft.view.viewModel.BoardsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BoardsFragment : Fragment(),BoardsAdapter.OnItemClickListener, AddBottomSheetFragment.OnItemClickListener {

    private var _binding: FragmentBoardsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BoardsViewModel by viewModels()
    private lateinit var adapter: BoardsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBoardsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBoardsBinding.bind(view)
        initBoardsList()

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.title= "Boards"
        if(toolbar.menu.isEmpty()){
            toolbar.inflateMenu(R.menu.top_app_bar)
        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_new_board -> {
                    AddBottomSheetFragment(this@BoardsFragment).show(parentFragmentManager,"AddBottomSheet")
                }
            }
            true
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    private fun displayBoards(){
        viewModel.allBoardsOfUser.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()){
                binding.noBoards.visibility = View.GONE
            }
            adapter.setBoards(it)
            adapter.notifyDataSetChanged()
        })

    }

    private fun initBoardsList(){
        binding.boardsList.layoutManager =LinearLayoutManager(requireContext())
        adapter = BoardsAdapter(this@BoardsFragment)
        binding.boardsList.adapter =  adapter
        displayBoards()
    }

    override fun onItemClick(projectName: String, projectID: Int,projectCode:String) {
        val intent = Intent(requireContext(), TasksActivity::class.java).apply {
            putExtra("projectName", projectName)
            putExtra("projectID", projectID)
            putExtra("projectCode",projectCode)
        }
        startActivity(intent)
    }
    override fun joinBoard(value: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val projectID = viewModel.getProjectIdByProjectCode(value)
            if (projectID != null) {
                viewModel.insertUserProjectCrossRef(projectID)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), "No Board Exists For That Code", Toast.LENGTH_SHORT).show()
            }
        }
    }

}