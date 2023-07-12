package com.example.boardsdraft.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentTaskInfoBinding
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.view.activities.TaskManagerActivity
import com.example.boardsdraft.view.activities.TasksActivity
import com.example.boardsdraft.view.viewModel.TaskInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskInfoFragment(
    private val task: Task
) : Fragment(), MyDialogFragment.OnItemClickListener {

    private var _binding: FragmentTaskInfoBinding? = null

    private val binding get() = _binding!!

    private val viewModel: TaskInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTaskInfoBinding.bind(view)

        val toolBar: androidx.appcompat.widget.Toolbar =
            requireActivity().findViewById(R.id.toolbar)
        toolBar.title = task.taskName
        if (toolBar.menu.isEmpty()) {
            if (task.createdByID == viewModel.getCurrentUserID()) {
                toolBar.inflateMenu(R.menu.profile_menu_item)
            }
        }
        toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    val editTaskDetailsFragment = EditTaskDetailsFragment(task)

                    if (activity is TaskManagerActivity) {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.task_manager_fragment_container, editTaskDetailsFragment)
                            .addToBackStack("EditTaskDetailsFragment").commit()
                    } else if (activity is TasksActivity) {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.tasks_view, editTaskDetailsFragment)
                            .addToBackStack("EditTaskDetailsFragment").commit()
                    }
                }
            }
            true
        }


        binding.apply {
            taskName.text = task.taskName
            projectName.text = task.projectName
            createdBy.text = task.createdBy
            currentStatus.text = task.status
            taskPriority.text = task.priority
            createdOn.text = task.createdDate
            deadline.text = task.deadLine
            assignedTo.text = task.assignedToName


            moveTo.apply {
                keyListener = null
                setOnItemClickListener { parent, _, position, _ ->
                    val value = parent.getItemAtPosition(position) as TaskTitles
                    setText(value.taskTitle)
                }
            }

            tasksSaveChangesButton.setOnClickListener {
                if (moveTo.text.isNullOrEmpty()) {
                    moveToLayout.error = "Select A Status"
                } else {
                    task.status = moveTo.text.toString()
                    viewModel.updateTask(task)

                    if (activity is TaskManagerActivity) {
                        requireActivity().finish()
                    }
                    if (activity is TasksActivity) {
                        parentFragmentManager.popBackStack()
                    }

                }
            }
            btnDeleteCard.setOnClickListener {
                MyDialogFragment(
                    "Are You Sure You Want To Delete this Task?", "Delete",
                    this@TaskInfoFragment
                )
                    .show(parentFragmentManager, "deleteTaskDialog")
            }
        }


    }

    override fun onResume() {
        super.onResume()

        viewModel.getTaskTitlesOfProject(task.projectID).observe(viewLifecycleOwner,
            Observer {
                if (it != null) {

                    if (viewModel.getCurrentUserID() == task.createdByID) {
                        binding.apply {
                            moveTo.setAdapter(
                                TitlesArrayAdapter(
                                    requireContext(),
                                    it[0].taskTitles
                                )
                            )
                            moveToLayout.visibility = View.VISIBLE
                            tasksSaveChangesButton.visibility = View.VISIBLE
                            btnDeleteCard.visibility = View.VISIBLE
                        }
                    }

                    if (viewModel.getCurrentUserID() == task.assignedTo) {
                        binding.apply {
                            moveTo.setAdapter(
                                TitlesArrayAdapter(
                                    requireContext(),
                                    it[0].taskTitles
                                )
                            )
                            moveToLayout.visibility = View.VISIBLE
                            tasksSaveChangesButton.visibility = View.VISIBLE
                        }
                    }
                }
            })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).apply {
            menu.clear()
            inflateMenu(R.menu.menu_for_tasks)
            title = task.projectName
        }


    }

    override fun result(choice: String) {
        if (choice == "YES") {
            viewModel.deleteTask(task)

            if (activity is TaskManagerActivity) {
                requireActivity().finish()
            }
            if (activity is TasksActivity) {
                parentFragmentManager.popBackStack()
            }


        }
    }

}

private class TitlesArrayAdapter(
    private val context: Context,
    private val titles: List<TaskTitles?>
) : ArrayAdapter<TaskTitles>(context, R.layout.item_priority_color, titles) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item_priority_color, null)
        val status: TextView = view.findViewById(R.id.drop_down_tv)
        println(titles[position]!!.taskTitle)
        status.text = titles[position]!!.taskTitle

        return view
    }
}