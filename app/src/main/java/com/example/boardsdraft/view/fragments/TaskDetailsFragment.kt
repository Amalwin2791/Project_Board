package com.example.boardsdraft.view.fragments

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.FragmentTaskDetailsBinding
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
class TaskDetailsFragment : Fragment() {

    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : NewTaskViewModel by viewModels()

    private var taskID : Int = 0

    private var assignedToName:String? = null
    private var assignedToID:Int? = null
    private var assignedDate: String? = null
    private var creationDate : String? = null
    private var assignedToEmail: String? = null
    private var selectedDate: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllUsersOfProject(requireArguments().getInt("projectID"))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTaskDetailsBinding.bind(view)

        createNotificationChannel()

        viewModel.lastTaskID.observe(viewLifecycleOwner, Observer {
            taskID = viewModel.lastTaskID.value?.plus(1) ?: 1
        })

        binding.apply {
            selectMemberForTask.apply{
                setOnItemClickListener { parent, _, position, _ ->
                    val value =  parent.getItemAtPosition(position) as User
                    setText(value.userName)
                    assignedToName = value.userName
                    assignedToID = value.userID
                    assignedToEmail = value.email
                }
            }
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)

            this.selectedDate = selectedDate.timeInMillis

            val currentDate = Calendar.getInstance()

            if (selectedDate.before(currentDate)) {
                Toast.makeText(requireContext(), "Please select a date after today", Toast.LENGTH_SHORT).show()
                return@OnDateSetListener
            }

            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
            binding.tvSelectDueDate.text = formattedDate
            assignedDate = formattedDate
        }

        binding.tvSelectDueDate.setOnClickListener {
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)+1
            val day = currentDate.get(Calendar.DAY_OF_MONTH)
            creationDate = "$day/$month/$year"


            val datePickerDialog = DatePickerDialog(requireContext(), dateSetListener, year, month, day)

            datePickerDialog.datePicker.minDate = currentDate.timeInMillis

            datePickerDialog.show()

            binding.tvSelectDueDate.error = null
        }


        binding.apply {
            btnUpdateCardDetails.setOnClickListener {

                if(validate()){
                    val task= Task(taskName = etNameCardDetails.text.toString().trim(),
                        projectID = requireArguments().getInt("projectID"), projectName = requireArguments().getString("projectName",null),
                    assignedTo = assignedToID!!, assignedToName = assignedToName!!,
                    createdBy = viewModel.getCurrentUserName()!!, createdByID = viewModel.getCurrentUserID(),status = requireArguments().getString("taskTitle",null),
                        priority = selectPriorityColor.text.toString(), createdDate = creationDate!!, deadLine = assignedDate!!, taskID = taskID)

                    viewModel.insertTask(task)
                    parentFragmentManager.popBackStack()

                    scheduleNotification()
                }
            }

            etNameCardDetails.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    taskNameLayout.error = null
                }
            }

            selectPriorityColor.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    selectPriorityLayout.error = null
                }
            }

            selectMemberForTask.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    selectMemberLayout.error = null
                }
            }

        }

    }

    private fun scheduleNotification() {

        val intent = Intent(requireContext().applicationContext,Notification::class.java)
        val title =  "Task Deadline Alert"
        val message = "The Task ${binding.etNameCardDetails.text.toString().trim()} is due Today."
        intent.putExtra(titleExtra,title)
        intent.putExtra(messageExtra,message)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            selectedDate,
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

        val notificationManager = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    private fun validate():Boolean{
        binding.apply {
            when{
                etNameCardDetails.text.isNullOrBlank()->{
                    taskNameLayout.error = "Task Name Cannot Be Empty"
                }
                selectPriorityColor.text.isNullOrBlank()->{
                    selectPriorityLayout.error ="Select A Priority"
                }

                selectMemberForTask.text.isNullOrBlank()->{
                    selectMemberLayout.error= "Select A Member"
                }
                assignedDate.isNullOrBlank()->{
                    tvSelectDueDate.error = "Select A Deadline"
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

        val colors = resources.getStringArray(R.array.priorities)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.item_priority_color,colors)
        binding.selectPriorityColor.apply {
            setAdapter(arrayAdapter)
            keyListener = null
        }

        viewModel.membersOfProject.observe(viewLifecycleOwner, Observer {
            binding.selectMemberForTask.apply {
                setAdapter(MembersArrayAdapter(requireContext(),it as ArrayList<User>))
                keyListener=null

            }
        })

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
private class MembersArrayAdapter(private val context : Context,private val userList : ArrayList<User>): ArrayAdapter<User>(context,R.layout.item_priority_color,userList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item_priority_color,null)
        val name: TextView = view.findViewById(R.id.drop_down_tv)

        name.text = userList[position].userName

        return view
    }

}
