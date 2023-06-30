package com.example.boardsdraft.view.fragments

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentEditTaskDetailsBinding
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.view.Notification
import com.example.boardsdraft.view.channelID
import com.example.boardsdraft.view.messageExtra
import com.example.boardsdraft.view.notificationID
import com.example.boardsdraft.view.titleExtra
import com.example.boardsdraft.view.viewModel.NewTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class EditTaskDetailsFragment(
    private val task: Task
) : Fragment() {

    private var _binding: FragmentEditTaskDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : NewTaskViewModel by viewModels()
    private var assignedDate: String? = null
    private var selectedDateLong: Long = 0

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditTaskDetailsBinding.bind(view)

        createNotificationChannel()

        val toolBar: Toolbar = requireActivity().findViewById(R.id.toolbar)
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

            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                selectedDateLong = selectedDate.timeInMillis

                val currentDate = Calendar.getInstance()

                if (selectedDate.before(currentDate)) {
                    Toast.makeText(requireContext(), "Please select a date after today", Toast.LENGTH_SHORT).show()
                    return@OnDateSetListener
                }

                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
                editTaskDueDate.text = formattedDate
                assignedDate = formattedDate
            }

            editTaskDueDate.setOnClickListener {
                val currentDate = Calendar.getInstance()
                val year = currentDate.get(Calendar.YEAR)
                val month = currentDate.get(Calendar.MONTH)+1
                val day = currentDate.get(Calendar.DAY_OF_MONTH)



                val datePickerDialog = DatePickerDialog(requireContext(), dateSetListener, year, month, day)

                datePickerDialog.datePicker.minDate = currentDate.timeInMillis

                datePickerDialog.show()
            }




            btnEditTaskDetails.setOnClickListener {
                if(validate()){
                    task.taskName = editTaskName.text.toString().trim()
                    task.priority = editTaskPriority.text.toString()
                    task.deadLine = editTaskDueDate.text.toString()
                    viewModel.updateTask(task)
                    parentFragmentManager.popBackStack()

                    scheduleNotification()
                }
            }

            editTaskName.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus){
                    editTaskNameLayout.error= null
                }
            }

            editTaskPriority.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus){
                    editTaskPriorityLayout.error= null
                }
            }
            editTaskMember.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus){
                    editTaskMemberLayout.error= null
                }
            }
            editTaskDueDate.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus){
                    editTaskDueDate.error= null
                }
            }
        }

    }

    private fun scheduleNotification() {

        val intent = Intent(requireContext().applicationContext, Notification::class.java)
        val title =  "Task Deadline Alert"
        val message = "The Task ${binding.editTaskName.text.toString().trim()} of Board ${task.projectName} is due Today."
        intent.putExtra(titleExtra,title)
        intent.putExtra(messageExtra,message)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            selectedDateLong,
            pendingIntent
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Project Board Notification Channel"
        val description = "Deadline Alerts"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelID, name,importance)
        channel.description = description

        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
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

        if(requireActivity().findViewById<Toolbar>(R.id.toolbar).menu.isEmpty()){
            requireActivity().findViewById<Toolbar>(R.id.toolbar).inflateMenu(R.menu.profile_menu_item)
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