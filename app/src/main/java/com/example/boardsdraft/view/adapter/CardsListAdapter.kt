package com.example.boardsdraft.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boardsDraft.R
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks

class CardsListAdapter(
    private val context: Context
) : RecyclerView.Adapter<CardsListAdapter.CardsViewHolder>(){

    private val tasksList = ArrayList<ProjectWithTasks>()

    fun setCardsList(tasks: List<ProjectWithTasks>){
        this.tasksList.clear()
        this.tasksList.addAll(tasks)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        return CardsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task_card,parent,false))
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        val task = tasksList[0].tasks[position]
        holder.apply{
            cardName.text = task?.taskName
        }
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    inner class CardsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cardName: TextView = view.findViewById(R.id.tv_card_name)
    }
}