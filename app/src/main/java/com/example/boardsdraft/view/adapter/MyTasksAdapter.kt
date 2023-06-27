package com.example.boardsdraft.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.boardsDraft.R
import com.example.boardsdraft.db.entities.Task

class MyTasksAdapter(private val clickListener: OnItemClickListener): RecyclerView.Adapter<MyTasksAdapter.MyTasksViewHolder>() {

    private var myTasks = ArrayList<Task?>()

    inner class MyTasksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val card: CardView = itemView.findViewById(R.id.my_tasks_card)

        fun bind(task: Task) {
            val taskName: TextView = itemView.findViewById(R.id.tvTaskName)
            val projectName: TextView = itemView.findViewById(R.id.tvProjectName)
            val status: TextView = itemView.findViewById(R.id.tvStatus)
            val dueDate: TextView = itemView.findViewById(R.id.tvDeadline)
            val priority: TextView = itemView.findViewById(R.id.tvPriority)

            taskName.text = task.taskName
            status.text = task.status
            dueDate.text = task.deadLine
            projectName.text = task.projectName
            priority.text = task.priority

            val textColorRes = when (task.priority) {
                "High" -> R.color.red
                "Medium" -> R.color.yellow
                "Low" -> R.color.green
                else -> R.color.black
            }

            priority.setTextColor(ContextCompat.getColor(priority.context, textColorRes))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTasksViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_my_tasks_card,parent,false)
        return MyTasksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myTasks.size
    }

    override fun onBindViewHolder(holder: MyTasksViewHolder, position: Int) {
        holder.bind(myTasks[position]!!)

        holder.card.apply {
            setOnClickListener {

                clickListener.onItemClick(myTasks[position]!!.taskID,myTasks[position]!!.taskName)
            }
        }
    }

    fun setMyTasks(myTasks: List<Task?>){
        this.myTasks.clear()
        this.myTasks.addAll(myTasks)
    }

    interface OnItemClickListener {
        fun onItemClick(taskID: Int,taskName: String)
    }
}