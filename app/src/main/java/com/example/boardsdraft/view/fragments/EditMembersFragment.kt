package com.example.boardsdraft.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentEditMembersBinding
import com.example.boardsdraft.view.adapter.EditMembersAdapter
import com.example.boardsdraft.view.adapter.MembersListAdapter
import com.example.boardsdraft.view.viewModel.MembersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditMembersFragment : Fragment(), EditMembersAdapter.OnItemClickListener,MyDialogFragment.OnItemClickListener{
    private var _binding: FragmentEditMembersBinding?= null
    private val binding get() = _binding!!
    private val viewModel: MembersViewModel by viewModels()
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
        requireActivity().findViewById<Toolbar>(R.id.members_toolbar).menu.clear()

        binding.editMembersList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            editMembersListAdapter = EditMembersAdapter(this@EditMembersFragment)
            adapter = editMembersListAdapter
        }

        viewModel.membersOfProject.observe(viewLifecycleOwner, Observer {
            viewModel.membersOfProject.value?.let { it1 ->

                editMembersListAdapter.setUsers(it1).apply {
                    editMembersListAdapter.setCurrentUser(viewModel.getCurrentUserID())
                    editMembersListAdapter.notifyDataSetChanged()
                }

            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        requireActivity().findViewById<Toolbar>(R.id.members_toolbar).inflateMenu(R.menu.profile_menu_item)
    }

    override fun onItemClick(userID: Int) {
        MyDialogFragment("Are You Sure You Want To delete this List?","Delete",
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