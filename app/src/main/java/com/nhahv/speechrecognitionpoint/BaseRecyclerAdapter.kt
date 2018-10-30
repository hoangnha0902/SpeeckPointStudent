package com.nhahv.speechrecognitionpoint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

open class BaseRecyclerAdapter<T>(
        val items: ArrayList<T>,
        @LayoutRes private val layoutRes: Int,
        val listener: ((View, T, Int) -> Unit)?
) : RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder>() {

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
        holder.itemView.setOnClickListener { view -> listener?.invoke(view, items[position], position) }
    }

    class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)
}