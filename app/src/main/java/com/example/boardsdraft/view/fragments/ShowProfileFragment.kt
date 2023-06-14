package com.example.boardsdraft.view.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentShowProfileBinding
import com.example.boardsdraft.view.adapter.ProjectsInCommonAdapter
import com.example.boardsdraft.view.viewModel.ShowProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowProfileFragment(
    private val editable:String
) : Fragment() {

    private var _binding: FragmentShowProfileBinding? =null
    private val binding get() = _binding!!

    private val viewModel: ShowProfileViewModel by viewModels()
    private lateinit var projectsInCommonAdapter: ProjectsInCommonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUser(requireArguments().getInt("userID"))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =FragmentShowProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentShowProfileBinding.bind(view)

        viewModel.dataMonitor.observe(viewLifecycleOwner) { isInitiated ->
            if (isInitiated) {

                binding.apply {
                    profileNameDisplay.text = viewModel.getCurrentUserName()
                    profileEmailDisplay.text = viewModel.getCurrentUserEmailID()


                    if (viewModel.getCurrentUserDesignation().isNullOrEmpty()) {
                        profileDesignationDisplay.text = resources.getString(R.string.unknown)
                    } else {
                        profileDesignationDisplay.text = viewModel.getCurrentUserDesignation()
                    }
                    if (viewModel.getCurrentUserDepartment().isNullOrEmpty()) {
                        profileOrganizationDisplay.text = resources.getString(R.string.unknown)
                    } else {
                        profileOrganizationDisplay.text = viewModel.getCurrentUserDepartment()
                    }
                    if (viewModel.getCurrentUserImage() != null) {
                        val image = viewModel.getCurrentUserImage()
                        profileImage.setImageBitmap(
                            BitmapFactory.decodeByteArray(
                                image,
                                0,
                                image!!.size
                            )
                        )
                    }

                    if (editable == "YES") {
                        projectsInCommonLayout.visibility = View.GONE
                    } else {
                        projectsInCommon.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            projectsInCommonAdapter = ProjectsInCommonAdapter()
                            adapter = projectsInCommonAdapter

                            viewModel.getProjectsInCommon(requireArguments().getInt("userID"))
                            viewModel.projectsInCommon.observe(viewLifecycleOwner) { commonProjects ->
                                if (commonProjects != null) {
                                    projectsInCommonAdapter.setList(commonProjects)
                                    projectsInCommonAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }


}