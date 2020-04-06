package com.diamondedge.ktadapter

import android.view.ViewGroup

open class KtMutableListAdapter<T : Any>(protected val values: MutableList<T>) : KtAdapter<T>() {

    override fun getItemCount() = values.size

    override fun get(index: Int): T {
        return values[index]
    }

    fun add(item: T) {
        values.add(item)
        notifyItemInserted(itemCount)
    }

    fun add(index: Int, item: T) {
        var i = index
        if (i < 0)
            i = 0
        values.add(i, item)
        notifyItemInserted(i)
    }

    fun addAll(list: List<T>) {
        addAll(values.size, list)
    }

    fun addAll(index: Int, list: List<T>) {
        var i = index
        if (i < 0)
            i = 0
        values.addAll(i, list)
        notifyItemRangeInserted(i, list.size)
    }

    fun remove(index: Int): T {
        val item = values.removeAt(index)
        notifyItemRemoved(index)
        return item
    }

    fun removeRange(positionStart: Int, count: Int) {
        for (i in positionStart + count - 1 downTo positionStart) {
            values.removeAt(i)
        }
        notifyItemRangeRemoved(positionStart, count)
    }

    fun set(index: Int, item: T): T {
        values[index] = item
        notifyItemChanged(index)
        return item
    }

    fun clear() {
        values.clear()
        notifyDataSetChanged()
    }

    fun indexOf(item: T): Int {
        return values.indexOf(item)
    }

    var items: List<T>
        get() = values.toList()
        set(list) {
            values.clear()
            values.addAll(list)
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return STRING_TYPE
    }

    override fun createVH(parent: ViewGroup, viewType: Int, adapter: KtAdapter<T>): ViewHolder<T> {
        return TextVH(parent, adapter)
    }
}