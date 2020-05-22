package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel
import com.softrasol.ahmedgulsaqib.digitaltasker.R

class RecyclerViewPractice(private val photos: List<UserDataModel>) :
    RecyclerView.Adapter<RecyclerViewPractice.ViewHolder>(){

    lateinit var context : Context
    lateinit var list : List<UserDataModel>

    init {
        this.context = context
        this.list = list
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_users_item_list, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = list[position]

        holder.mTxtName?.text = model.name

        holder.itemView.setOnClickListener(){

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var mTxtName : TextView
        init {
            mTxtName = itemView.findViewById(R.id.txt_view_user_name)
        }

    }
}