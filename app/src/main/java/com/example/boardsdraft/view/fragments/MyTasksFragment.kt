package com.example.boardsdraft.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentMyTasksBinding
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.view.activities.TaskManagerActivity
import com.example.boardsdraft.view.adapter.MyTasksAdapter
import com.example.boardsdraft.view.viewModel.MyTasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class MyTasksFragment : Fragment(), MyTasksAdapter.OnItemClickListener {

    private var _binding : FragmentMyTasksBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MyTasksViewModel by viewModels()

    private lateinit var myTasksAdapter: MyTasksAdapter


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
        toolbar.apply {
            title= "My Tasks"
            menu.clear()
        }


        viewModel.allTasksOfCurrentUser.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                binding.myTasksList.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    myTasksAdapter = MyTasksAdapter(this@MyTasksFragment)
                    adapter = myTasksAdapter
                    myTasksAdapter.setMyTasks(sortTasks(it[0].tasks))
                }

                binding.apply {
                    noTasksHaveBeenAssigned.visibility = View.GONE
                    myTasksList.visibility = View.VISIBLE
                }
            }


        })




    }


    private fun sortTasks(tasks: List<Task?>): List<Task?> {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return tasks.sortedWith(compareBy<Task> { task ->
            when (task.priority) {
                "High" -> 0
                "Medium" -> 1
                else -> 2
            }
        }.thenComparing { task ->
            val taskDeadline = dateFormat.parse(task.deadLine)
            val diffInMilliseconds = taskDeadline?.time ?: (0 - currentDate.time)
            diffInMilliseconds.toInt()
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }

    override fun onItemClick(taskID: Int,taskName: String) {
        requireContext().startActivity(Intent(requireContext(),TaskManagerActivity::class.java).apply {
            putExtra("taskID",taskID)
            putExtra("taskName",taskName)
        })
    }

}
