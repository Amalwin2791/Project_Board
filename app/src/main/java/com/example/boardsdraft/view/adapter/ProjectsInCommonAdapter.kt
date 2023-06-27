package com.example.boardsdraft.view.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.boardsDraft.R
import com.example.boardsdraft.db.entities.Project

class ProjectsInCommonAdapter: RecyclerView.Adapter<ProjectsInCommonAdapter.ProjectsInCommonAdapterViewHolder>() {

    private val  projectsInCommon = ArrayList<Project>()

    inner class ProjectsInCommonAdapterViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val boardName: TextView = view.findViewById(R.id.name_minimal)
        private val boardImage: ImageView = view.findViewById(R.id.image_minimal)
        val card: CardView = view.findViewById(R.id.card_minimal)

        fun bind(project: Project){
            boardName.text = project.projectName
            project.image?.let {
                boardImage.setImageBitmap(BitmapFactory.decodeByteArray(project.image, 0, it.size))
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectsInCommonAdapterViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_card_minimal,parent,false)
        return ProjectsInCommonAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return projectsInCommon.size
    }

    override fun onBindViewHolder(holder: ProjectsInCommonAdapterViewHolder, position: Int) {
        holder.bind(projectsInCommon[position])

    }

    fun setList(projects :List<Project>){
        this.projectsInCommon.clear()
        this.projectsInCommon.addAll(projects)
    }
}