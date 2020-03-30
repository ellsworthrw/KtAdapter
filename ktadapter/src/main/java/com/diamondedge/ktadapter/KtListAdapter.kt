package com.diamondedge.ktadapter

import android.view.ViewGroup

open class KtListAdapter<T : Any>(protected val values: List<T>) : KtAdapter<T>() {

    override fun getItemCount() = values.size

    override fun get(index: Int): T {
        return values[index]
    }

    fun indexOf(item: T): Int {
        return values.indexOf(item)
    }

    override fun getItemViewType(position: Int): Int {
        return STRING_TYPE
    }

    override fun createVH(parent: ViewGroup, viewType: Int, adapter: KtAdapter<T>): ViewHolder<T> {
        return TextVH(parent, adapter)
    }
}