package com.example.boardsdraft.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boardsDraft.R
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks

class CardsListAdapter : RecyclerView.Adapter<CardsListAdapter.CardsViewHolder>(){

    private val tasksList = ArrayList<Task>()

    fun setCardsList(tasks: List<Task>){
        this.tasksList.clear()
        this.tasksList.addAll(tasks)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        return CardsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_card,parent,false))
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        if(tasksList.isNotEmpty()){
            holder.bind(tasksList[position])
        }
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }



    inner class CardsViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val cardName: TextView = view.findViewById(R.id.tv_card_name)

        fun bind(task: Task){
            cardName.text = task.taskName
        }
    }
}