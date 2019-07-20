package com.diamondedge.ktadapter

import android.content.res.Resources
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

class StringVH<T : Any>(val parent: View, adapter: KtAdapter<T>) :
    KtAdapter.ViewHolder<T>(LinearLayout(parent.context), adapter) {

    private val textView: TextView = TextView(parent.context)

    init {
        textView.isFocusable = true
        textView.isFocusableInTouchMode = true
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setTextAppearance(parent.context, android.R.style.TextAppearance_Inverse);
        val container = itemView
        if (container is LinearLayout) {
            val layoutParams =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val margin = 8.px
            layoutParams.setMargins(margin, margin / 2, margin, margin / 2)
            container.layoutParams = layoutParams
            container.addView(textView)
        }
    }

    override fun bind(position: Int, item: T) {
        textView.text = item.toString()
    }
}

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
