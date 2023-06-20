package com.example.boardsdraft.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentEditTaskDetailsBinding
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.view.viewModel.NewTaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTaskDetailsFragment(
    private val task: Task
) : Fragment() {

    private var _binding: FragmentEditTaskDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : NewTaskViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllUsersOfProject(task.projectID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTaskDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditTaskDetailsBinding.bind(view)

        val toolBar: Toolbar = requireActivity().findViewById(R.id.task_manager_toolbar)
        toolBar.menu.clear()
        binding.apply {
            editTaskName.setText(task.taskName)
            editTaskPriority.setText(task.priority)
            editTaskMember.setText(task.assignedToName)
            editTaskDueDate.text = task.deadLine

            editTaskMember.apply {
                setOnItemClickListener { parent, _, position, _ ->
                    val value =  parent.getItemAtPosition(position) as User
                    setText(value.userName)
                    task.assignedTo = value.userID
                    task.assignedToName =value.userName
                }
            }

            btnEditTaskDetails.setOnClickListener {
                if(validate()){
                    task.taskName = editTaskName.text.toString().trim()
                    task.priority = editTaskPriority.text.toString()
                    task.deadLine = editTaskDueDate.text.toString()
                    viewModel.updateTask(task)
                    parentFragmentManager.popBackStack()
                }
            }
        }

    }

    private fun validate():Boolean{
        binding.apply {
            when{
                editTaskName.text.isNullOrBlank()->{
                    editTaskNameLayout.error = "Task Name Cannot Be Empty"
                }
                editTaskPriority.text.isNullOrBlank()->{
                    editTaskPriorityLayout.error ="Select A Priority"
                }
                editTaskMember.text.isNullOrBlank()->{
                    editTaskMemberLayout.error= "Select A Member"
                }
                editTaskDueDate.text.isNullOrBlank()->{
                    editTaskDueDate.error = "Select A Deadline"
                }
                else->{
                    return true
                }
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()

        val priorities = resources.getStringArray(R.array.priorities)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.item_priority_color,priorities)
        binding.editTaskPriority.apply {
            setAdapter(arrayAdapter)
            keyListener = null
        }

        viewModel.membersOfProject.observe(viewLifecycleOwner, Observer {
            binding.editTaskMember.apply {
                setAdapter(EditMembersArrayAdapter(requireContext(),it as ArrayList<User>))
                keyListener = null
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

        if(requireActivity().findViewById<Toolbar>(R.id.task_manager_toolbar).menu.isEmpty()){
            requireActivity().findViewById<Toolbar>(R.id.task_manager_toolbar).inflateMenu(R.menu.profile_menu_item)
        }

    }


}

private class EditMembersArrayAdapter(private val context : Context, private val userList : ArrayList<User>): ArrayAdapter<User>(context,R.layout.item_priority_color,userList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item_priority_color,null)
        val name: TextView = view.findViewById(R.id.drop_down_tv)

        name.text = userList[position].userName

        return view
    }

}