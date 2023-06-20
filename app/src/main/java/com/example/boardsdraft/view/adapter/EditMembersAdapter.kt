package com.example.boardsdraft.view.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.boardsDraft.R
import com.example.boardsdraft.db.entities.User

class EditMembersAdapter(
    private val clickListener: OnItemClickListener
): RecyclerView.Adapter<EditMembersAdapter.EditMembersViewHolder>(){

    private val users = ArrayList<User>()
    private var numberOfUsers: Int =0
    private var currentUserID : Int = -1

    inner class EditMembersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val name: TextView = itemView.findViewById(R.id.edit_member_name)
        private val image: ImageView = itemView.findViewById(R.id.edit_member_image)
        val card: CardView = itemView.findViewById(R.id.edit_members_card)
        val deleteButton: ImageButton =  itemView.findViewById(R.id.ib_edit_delete_member)

        fun bind(user: User){

            if(user.userID == currentUserID){
                name.text = name.context.resources.getString(R.string.you)
                deleteButton.visibility= View.GONE
            }
            else{
                name.text = user.userName
            }
            if(user.image != null){
                image.setImageBitmap(BitmapFactory.decodeByteArray(user.image, 0, user.image!!.size))
            }


        }
    }

    fun setUsers(users: List<User>){
        this.users.clear()
        this.users.addAll(users)
        numberOfUsers= this.users.size
    }

    fun setCurrentUser(userID: Int){
        currentUserID = userID
    }

    interface OnItemClickListener {
        fun onItemClick(userID: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditMembersViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_edit_members_card,parent,false)
        return EditMembersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return numberOfUsers
    }

    override fun onBindViewHolder(holder: EditMembersViewHolder, position: Int) {
        holder.bind(users[position])


        holder.card.apply {
            setBackgroundResource(R.drawable.shape_button_curved_white)
            elevation=30F
            setOnClickListener {
                clickListener.onItemClick(users[position].userID)
            }
        }
    }
}