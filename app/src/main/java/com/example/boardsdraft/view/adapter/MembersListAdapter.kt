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
import com.example.boardsdraft.db.entities.User

class MembersListAdapter(
    private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<MembersListAdapter.MembersListAdapterViewHolder>() {

    private val users = ArrayList<User>()
    private var numberOfUsers: Int = 0
    private var currentUserID: Int = -1

    inner class MembersListAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.member_name)
        private val image: ImageView = itemView.findViewById(R.id.member_image)
        val card: CardView = itemView.findViewById(R.id.members_card)


        fun bind(user: User) {

            if (user.userID == currentUserID) {
                name.text = name.context.resources.getString(R.string.you)
            } else {
                name.text = user.userName
            }
            if (user.image != null) {
                image.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        user.image,
                        0,
                        user.image!!.size
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MembersListAdapterViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_members_card, parent, false)
        return MembersListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return numberOfUsers
    }

    override fun onBindViewHolder(holder: MembersListAdapterViewHolder, position: Int) {


        holder.bind(users[position])


        holder.card.apply {
            setOnClickListener {
                clickListener.onItemClick(users[position].userID)
            }
        }
    }

    fun setUsers(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)
        numberOfUsers = this.users.size
    }

    fun setCurrentUser(userID: Int) {
        currentUserID = userID
    }

    interface OnItemClickListener {
        fun onItemClick(userID: Int)
    }
}