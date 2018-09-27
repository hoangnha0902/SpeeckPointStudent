package com.nhahv.speechrecognitionpoint

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

open class BaseRecyclerViewAdapter<T>(
        val items: ArrayList<T>,
        @LayoutRes private val layoutRes: Int,
        private val listener: OnItemListener<T>
) : RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder>() {

    private var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseViewHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(p0.context)
        }
        val view = inflater?.inflate(layoutRes, p0, false)
        return BaseViewHolder(view!!)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.setOnClickListener { listener.onClick(items[position], position) }
    }

    class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)


    interface OnItemListener<T> {
        fun onClick(item: T, position: Int)
    }
}