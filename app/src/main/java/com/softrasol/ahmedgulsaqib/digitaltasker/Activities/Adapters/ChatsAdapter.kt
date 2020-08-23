package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.ChatModel
import com.softrasol.ahmedgulsaqib.digitaltasker.R
import de.hdodenhof.circleimageview.CircleImageView

class ChatsAdapter (list : List<ChatModel>,context: Context) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {

    var list: List<ChatModel>
    var context: Context

    val MSG_TYPE_LEFT = 0
    val MSG_TYPE_RIGHT = 1

    var fUser = ""

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MSG_TYPE_LEFT){
            var view = LayoutInflater.from(context)
                .inflate(R.layout.chat_sender_layout, parent, false)
            return ViewHolder(view)
        }else{
            var view = LayoutInflater.from(context)
                .inflate(R.layout.chat_reciever_layout, parent, false)

            return ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var model = list.get(position)

        holder.message.text = model.message
    }


    override fun getItemViewType(position: Int): Int {

        fUser = FirebaseAuth.getInstance().uid.toString()

        if (list.get(position).equals(fUser)){
            return MSG_TYPE_RIGHT
        }

        return MSG_TYPE_LEFT

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var message : TextView
        lateinit var image : CircleImageView

        init {
            message = itemView.findViewById(R.id.show_msg)
        }

    }

}