package com.demo.cl.mapbox.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.cl.mapbox.BR
import com.demo.cl.mapbox.R
import com.demo.cl.mapbox.db.Pin

class PinAdapter : ListAdapter<Pin, BindingViewHolder>(object : DiffUtil.ItemCallback<Pin?>() {
    override fun areItemsTheSame(oldItem: Pin, newItem: Pin): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Pin, newItem: Pin): Boolean {
        return true
    }
}) {
    var onItemClickFun:((view: View, data: Pin)->Unit)?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return BindingViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.item_pin,parent,false))
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        holder.bind(BR.pin,getItem(position),onItemClickFun)
    }
}



class BindingViewHolder(private val dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {
    fun <T> bind(id: Int, data: T,onItemClick:((view: View,data:T)->Unit)?=null) {
        dataBinding.setVariable(id, data)
        dataBinding.executePendingBindings()
        onItemClick?.let {onItemClickFun->
            dataBinding.root.setOnClickListener {
                onItemClickFun(it,data)
            }
        }

    }
}