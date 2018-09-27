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
) : RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<T>>() {

    private var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseViewHolder<T> {
        if (inflater == null) {
            inflater = LayoutInflater.from(p0.context)
        }
        val view = inflater?.inflate(layoutRes, p0, false)
        return BaseViewHolder(view!!, items, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
    }

    class BaseViewHolder<T>(view: View,
                            items: ArrayList<T>,
                            listener: OnItemListener<T>
    ) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener { listener.onClick(items[adapterPosition], adapterPosition) }
        }
    }

    interface OnItemListener<T> {
        fun onClick(item: T, position: Int)
    }
}