package com.diamondedge.ktadapter

import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView

typealias ClickListener<T> = (v: View, vh: KtAdapter.ViewHolder<T>, adapter: KtAdapter<T>) -> Unit

typealias LongClickListener<T> = (v: View, vh: KtAdapter.ViewHolder<T>, adapter: KtAdapter<T>) -> Boolean

typealias FocusListener<T> = (hasFocus: Boolean, v: View, vh: KtAdapter.ViewHolder<T>, adapter: KtAdapter<T>) -> Boolean

typealias KeyListener<T> = (keyCode: Int, event: KeyEvent, v: View, vh: KtAdapter.ViewHolder<T>, adapter: KtAdapter<T>) -> Boolean

typealias SelectListener<T> = (position: Int, item: T?) -> Unit

typealias CreateViewHolderListener<T> = (vh: KtAdapter.ViewHolder<T>) -> Unit
typealias BindViewHolderListener<T> = (vh: KtAdapter.ViewHolder<T>, position: Int, item: T) -> Unit
typealias UnbindViewHolderListener<T> = (vh: KtAdapter.ViewHolder<T>) -> Unit

abstract class KtAdapter<T : Any> : RecyclerView.Adapter<KtAdapter.ViewHolder<T>>() {
    var clickListener: ClickListener<T>? = null
    var longClickListener: LongClickListener<T>? = null
    var focusListener: FocusListener<T>? = null
    var keyListener: KeyListener<T>? = null
    var createViewHolderListener: CreateViewHolderListener<T>? = null
    var bindViewHolderListener: BindViewHolderListener<T>? = null
    var unbindViewHolderListener: UnbindViewHolderListener<T>? = null

    abstract operator fun get(index: Int): T

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val vh = createVH(parent, viewType, this)
        val view = vh.itemView
        view.setOnClickListener(vh)
        view.setOnLongClickListener(vh)
        view.setOnFocusChangeListener(vh)
        view.setOnKeyListener(vh)
        createViewHolderListener?.invoke(vh)
        return vh
    }

    abstract fun createVH(parent: ViewGroup, viewType: Int, adapter: KtAdapter<T>): ViewHolder<T>

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        val item = get(position)
        Log.i("KtAdapter", "onBindViewHolder item($position) $item count: $itemCount")
        holder.bind(position, item)
        bindViewHolderListener?.invoke(holder, position, item)
    }

    open fun onUnbindViewHolder(holder: ViewHolder<T>) {
        unbindViewHolderListener?.invoke(holder)
    }

    override fun onViewRecycled(holder: ViewHolder<T>) {
        holder.unbind()
        onUnbindViewHolder(holder)
    }

    open class ViewHolder<T : Any>(itemView: View, val adapter: KtAdapter<T>) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener,
            View.OnLongClickListener,
            View.OnFocusChangeListener,
            View.OnKeyListener {

        open fun bind(position: Int, item: T) {}
        open fun unbind() {}

        override fun onClick(v: View) {
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
