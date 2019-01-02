package com.diamondedge.ktadapter

import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

typealias ClickListener<T> = (v: View, vh: KtAdapter.ViewHolder<T>, adapter: KtAdapter<T>) -> Unit

typealias LongClickListener<T> = (v: View, vh: KtAdapter.ViewHolder<T>, adapter: KtAdapter<T>) -> Boolean

typealias FocusListener<T> = (hasFocus: Boolean, v: View, vh: KtAdapter.ViewHolder<T>, adapter: KtAdapter<T>) -> Boolean

typealias KeyListener<T> = (keyCode: Int, event: KeyEvent, v: View, vh: KtAdapter.ViewHolder<T>, adapter: KtAdapter<T>) -> Boolean

typealias SelectListener<T> = (position: Int, item: T?) -> Unit

abstract class KtAdapter<T : Any> : RecyclerView.Adapter<KtAdapter.ViewHolder<T>>() {
    var clickListener: ClickListener<T>? = null
    var longClickListener: LongClickListener<T>? = null
    var focusListener: FocusListener<T>? = null
    var keyListener: KeyListener<T>? = null

    abstract operator fun get(index: Int): T

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KtAdapter.ViewHolder<T> {
        val vh = createVH(parent, viewType, this)
        val view = vh.itemView
        view.setOnClickListener(vh)
        view.setOnLongClickListener(vh)
        view.setOnFocusChangeListener(vh)
        view.setOnKeyListener(vh)
        // click/focus/key listeners should never be set after onCreateViewHolder!!!!
        // this is why we use a delegate
        return vh
    }

    abstract fun createVH(parent: ViewGroup, viewType: Int, adapter: KtAdapter<T>): ViewHolder<T>

    override fun onBindViewHolder(holder: KtAdapter.ViewHolder<T>, position: Int) {
        val item = get(position)
        Log.i("KtAdapter", "onBindViewHolder item($position) $item count: $itemCount")
        holder.bind(position, item)
    }

    open class ViewHolder<T : Any>(itemView: View, val adapter: KtAdapter<T>) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener,
            View.OnLongClickListener,
            View.OnFocusChangeListener,
            View.OnKeyListener {

        open fun bind(position: Int, item: T) {}
        open fun unbind() {}

        final override fun onClick(v: View) {
            adapter.clickListener?.invoke(v, this, adapter)
        }

        override fun onLongClick(v: View): Boolean {
            return adapter.longClickListener?.invoke(v, this, adapter) ?: false
        }

        override fun onFocusChange(v: View, hasFocus: Boolean) {
//            adapter.onFocusChange(hasFocus, v, this)
            adapter.focusListener?.invoke(hasFocus, v, this, adapter)
        }

        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
            return adapter.keyListener?.invoke(keyCode, event, v, this, adapter) ?: false
        }
    }
}
