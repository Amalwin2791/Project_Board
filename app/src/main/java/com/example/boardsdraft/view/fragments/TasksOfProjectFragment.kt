package com.example.boardsdraft.view.fragments

import  android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentTasksOfProjectBinding
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.view.adapter.TaskListAdapter
import com.example.boardsdraft.view.viewModel.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksOfProjectFragment : Fragment(), TaskListAdapter.OnItemClickListener,
    MyDialogFragment.OnItemClickListener {

    private var _binding: FragmentTasksOfProjectBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TasksViewModel by viewModels()
    private lateinit var taskListAdapter: TaskListAdapter
    private var taskTitleID: Int = 0
    private var taskTitle: TaskTitles? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksOfProjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTasksOfProjectBinding.bind(view)
        viewModel.getAllUsersOfProject(requireArguments().getInt("projectID", 0))
        viewModel.getAllTaskTitleNames(requireArguments().getInt("projectID", 0))

        viewModel.lastTaskTitleID.observe(viewLifecycleOwner){
            id->
            id?.let {
                viewModel.lastId = id+1
            }
        }

        taskListAdapter = TaskListAdapter(
            this@TasksOfProjectFragment,
            taskTitleID,
            requireArguments().getInt("projectID", 0)
        )

        viewModel.taskTitlesOfProject.observe(viewLifecycleOwner){list->
            taskDetailsInit()

            if(list!=null){
                viewModel.taskTitles = list
            }
        }





    }

    private fun taskDetailsInit() {
        binding.taskList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)


            adapter = taskListAdapter
        }

        displayTasks()

    }

    private fun displayTasks() {
        viewModel.allTasksOfDisplayedProject(requireArguments().getInt("projectID", 0))
            .observe(viewLifecycleOwner,
                Observer {
                    Log.d(TAG, "displayTasks: $it")
                    if (it != null) {
                        taskListAdapter.setTaskList(it)
                        taskListAdapter.notifyDataSetChanged()

                    }

                })
        viewModel.getTaskTitlesOfProject(requireArguments().getInt("projectID", 0))
            .observe(viewLifecycleOwner,
                Observer {
                    if (it != null) {
                        taskListAdapter.setTaskTitlesList(it)
                        taskListAdapter.notifyDataSetChanged()
                    }
                })

        viewModel.membersOfProject.observe(viewLifecycleOwner, Observer {
            viewModel.membersOfProject.value?.let {
                taskListAdapter.setMembers(it)
                taskListAdapter.notifyDataSetChanged()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.taskList.adapter=null
        _binding = null

    }

    override fun deleteTaskTitle(taskTitle: TaskTitles) {
        MyDialogFragment(
            "Are You Sure You Want To delete this List?, All The Tasks Under This List Will Also Be Deleted.",
            "Delete",
            this@TasksOfProjectFragment
        )
            .show(parentFragmentManager, "deleteDialog")
        this.taskTitle = taskTitle

    }


    override fun insertTaskTitle(newTaskTitleObj: TaskTitles) {


        if(!viewModel.taskTitles.contains(newTaskTitleObj.taskTitle)){

            newTaskTitleObj.taskTitleID = viewModel.lastId
            viewModel.insertTaskTitle(newTaskTitleObj)
            taskListAdapter.notifyDataSetChanged()
        }
        else{
            Toast.makeText(requireContext(), "Title Already Present", Toast.LENGTH_SHORT).show()
        }
    }

    override fun updateTaskTitle(taskTitle: TaskTitles, oldTitle: String?) {


        if(!viewModel.taskTitles.contains(taskTitle.taskTitle)){
            viewModel.insertTaskTitle(taskTitle)
            taskListAdapter.notifyDataSetChanged()
        }
        else{
            Toast.makeText(requireContext(), "Title Already Present", Toast.LENGTH_SHORT).show()
        }
    }

    override fun createNewTask(taskTitle: String) {
        val bundle = Bundle()
        val taskDetailsFragment = TaskDetailsFragment()
        bundle.putInt("projectID", requireArguments().getInt("projectID"))
        bundle.putString("taskTitle", taskTitle)
        bundle.putString("projectName", requireArguments().getString("projectName"))

        taskDetailsFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.tasks_view, taskDetailsFragment)
            .addToBackStack(null)
            .commit()
        taskListAdapter.notifyDataSetChanged()
    }

    override fun showTask(task: Task) {
        requireActivity().findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar).menu.clear()
        parentFragmentManager.beginTransaction()
            .replace(R.id.tasks_view, TaskInfoFragment(task))
            .addToBackStack("TaskInfoFragment")
            .commit()
    }


    override fun result(choice: String) {
        if (choice == "YES") {
            taskTitle?.let {
                viewModel.deleteTaskTitle(it)
                taskListAdapter.notifyDataSetChanged()
            }

        }
    }
}