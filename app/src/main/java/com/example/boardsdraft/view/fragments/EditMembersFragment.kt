package com.example.boardsdraft.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentEditMembersBinding
import com.example.boardsdraft.view.adapter.EditMembersAdapter
import com.example.boardsdraft.view.viewModel.EditMembersViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditMembersFragment : Fragment(), EditMembersAdapter.OnItemClickListener,MyDialogFragment.OnItemClickListener{
    private var _binding: FragmentEditMembersBinding?= null
    private val binding get() = _binding!!
    private val viewModel: EditMembersViewModel by viewModels()
    private lateinit var editMembersListAdapter: EditMembersAdapter
    private var userID: Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllUsersOfProject(requireArguments().getInt("projectID"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditMembersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditMembersBinding.bind(view)
        requireActivity().findViewById<Toolbar>(R.id.toolbar).apply {
            menu.clear()
            title = "Edit Members"
        }

        requireActivity().findViewById<FloatingActionButton>(R.id.members_fab).visibility = View.GONE

        binding.editMembersList.layoutManager = LinearLayoutManager(requireContext())
        editMembersListAdapter = EditMembersAdapter(this@EditMembersFragment)
        binding.editMembersList.adapter = editMembersListAdapter


        viewModel.membersOfProject.observe(viewLifecycleOwner, Observer {
            viewModel.membersOfProject.value?.let { it1 ->
                editMembersListAdapter.apply {
                    setUsers(it1)
                    setCurrentUser(viewModel.getCurrentUserID())
                    notifyDataSetChanged()
                }

            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        requireActivity().findViewById<LinearLayout>(R.id.members_linear_layout).visibility = View.VISIBLE
        requireActivity().findViewById<FloatingActionButton>(R.id.members_fab).visibility = View.VISIBLE
        requireActivity().findViewById<Toolbar>(R.id.toolbar).apply {
            inflateMenu(R.menu.profile_menu_item)
            title = "Members"
        }
    }

    override fun onItemClick(userID: Int) {
        MyDialogFragment("Are You Sure You Want To Remove this Member?","Remove",
            this@EditMembersFragment)
            .show(parentFragmentManager,"deleteDialog")
        this.userID = userID
    }

    override fun result(choice: String) {
        if (choice=="YES"){
            viewModel.removeUserFromProject(userID,requireArguments().getInt("projectID"))
        }
    }

}