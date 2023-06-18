package com.example.boardsdraft.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentTaskInfoBinding
import com.example.boardsDraft.databinding.FragmentTasksOfProjectBinding
import com.example.boardsDraft.databinding.ItemPriorityColorBinding
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.view.viewModel.MyTasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskInfoFragment(
    private val task: Task
) : Fragment() {

    private var _binding: FragmentTaskInfoBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MyTasksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskInfoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTaskInfoBinding.bind(view)



        binding.apply {
            taskName.text = task.taskName
            projectName.text = task.projectName
            createdBy.text =task.createdBy
            currentStatus.text = task.status
            taskPriority.text = task.priority
            createdOn.text = task.createdDate
            deadline.text = task.deadLine

            tasksSaveChangesButton.setOnClickListener {
                if(moveTo.text.isNullOrEmpty()){
                    moveToLayout.error = "Select A Status"
                }
                else{
                    task.status = moveTo.text.toString()
                    viewModel.updateTask(task)
                }
            }
        }



    }

    override fun onResume() {
        super.onResume()

        viewModel.getTaskTitlesOfProject(task.projectID).observe(viewLifecycleOwner,
            Observer {
                if(it != null){
                    if(it[0].taskTitles.size>1 && viewModel.getCurrentUserID() == task.assignedTo ){
                        binding.apply {
                            moveToLayout.visibility = View.VISIBLE
                            tasksSaveChangesButton.visibility = View.VISIBLE
                        }
                        binding.moveTo.apply {
                            setAdapter(TitlesArrayAdapter(requireContext(),it[0].taskTitles))
                        }
                    }
                }
            })

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

}

private class TitlesArrayAdapter(private val context: Context,private val titles: List<TaskTitles?>):ArrayAdapter<TaskTitles>(context,R.layout.item_priority_color,titles){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item_priority_color,null)
        val status: TextView = view.findViewById(R.id.drop_down_tv)
        status.text = titles[position]!!.taskTitle

        return view
    }
}