package com.example.boardsdraft.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentTasksOfProjectBinding
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.view.adapter.TaskListAdapter
import com.example.boardsdraft.view.viewModel.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksOfProjectFragment : Fragment() , TaskListAdapter.OnItemClickListener, MyDialogFragment.OnItemClickListener
{

    private var _binding : FragmentTasksOfProjectBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TasksViewModel by viewModels()
    private lateinit var taskListAdapter: TaskListAdapter
    private var taskTitleID :Int =0
    private var taskTitle:TaskTitles ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentTasksOfProjectBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTasksOfProjectBinding.bind(view)

        taskDetailsInit()

    }
     private fun taskDetailsInit() {
        binding.taskList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            viewModel.lastTaskTitleID.observe(viewLifecycleOwner, Observer {
                taskTitleID = viewModel.lastTaskTitleID.value?.plus(1)?:1
            })
            taskListAdapter = TaskListAdapter(this@TasksOfProjectFragment,taskTitleID,requireArguments().getInt("projectID",0))
            adapter = taskListAdapter
        }

        displayTasks()

    }
    private fun displayTasks() {
        viewModel.getTaskTitlesOfProject(requireArguments().getInt("projectID",0)).observe(viewLifecycleOwner,
            Observer {
                if(it != null){
                    taskListAdapter.setTaskTitlesList(it)
                    taskListAdapter.notifyDataSetChanged()
                }
            })
        viewModel.allTasksOfDisplayedProject(requireArguments().getInt("projectID",0)).observe(viewLifecycleOwner,
            Observer {
                if (it != null) {
                    taskListAdapter.setTaskList(it)
                    taskListAdapter.notifyDataSetChanged()

                }

            })

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun deleteTaskTitle(taskTitle: TaskTitles) {
        MyDialogFragment("Are You Sure You Want To delete this List?","Delete",
            this@TasksOfProjectFragment)
            .show(parentFragmentManager,"deleteDialog")
        this.taskTitle = taskTitle

    }

    override fun insertTaskTitle(taskTitle: TaskTitles) {
        viewModel.insertTaskTitle(taskTitle)
        taskListAdapter.notifyDataSetChanged()
    }

    override fun updateTaskTitle(taskTitle: TaskTitles) {
        viewModel.updateTaskTitle(taskTitle)
        taskListAdapter.notifyDataSetChanged()

    }

    override fun createNewTask() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.tasks_view,TaskDetailsFragment())
            .addToBackStack(null)
            .commit()
        taskListAdapter.notifyDataSetChanged()
    }

    override fun result(choice: String) {
        if(choice == "YES"){
            taskTitle?.let{
                viewModel.deleteTaskTitle(it)
            }

        }
    }


}