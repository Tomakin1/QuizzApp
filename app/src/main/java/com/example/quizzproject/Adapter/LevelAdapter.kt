package com.example.quizzproject.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzproject.R

class LevelAdapter(
    private val context:Context,
    private val onItemClick: (Int)-> Unit
): RecyclerView.Adapter<LevelAdapter.ViewHolder>() {
    class ViewHolder (itemView: View):RecyclerView.ViewHolder(itemView){
        val txtLevelName:TextView= itemView.findViewById(R.id.txtLevelName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(
           LayoutInflater.from(parent.context).inflate(R.layout.viewholder_level,parent,false)

       )
    }



    override fun onBindViewHolder(holder: LevelAdapter.ViewHolder, position: Int) {
        val mainPos=position+1
        holder.txtLevelName.text= "Level $mainPos"

        holder.itemView.setOnClickListener {
            Toast.makeText(context,mainPos.toString(),Toast.LENGTH_LONG)
        }

        holder.itemView.setOnClickListener {
            onItemClick(mainPos)
        }
    }

    override fun getItemCount(): Int {
        return 5
    }


}
