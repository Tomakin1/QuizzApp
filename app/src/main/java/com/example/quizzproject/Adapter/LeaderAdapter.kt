
package com.example.quizzproject.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzproject.Domain.UserModel
import com.example.quizzproject.databinding.ViewholderLeaderBinding

class LeaderAdapter : RecyclerView.Adapter<LeaderAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewholderLeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserModel, position: Int) {
            binding.apply {
                titleTxt.text = user.name

                rowTxt.text = (position + 4).toString()
                scoreTxt.text = user.total_score.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewholderLeaderBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}
