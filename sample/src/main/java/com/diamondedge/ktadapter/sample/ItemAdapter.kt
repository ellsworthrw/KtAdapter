package com.diamondedge.ktadapter.sample

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.diamondedge.ktadapter.KtAdapter
import com.diamondedge.ktadapter.KtListAdapter
import com.diamondedge.ktadapter.TextVH
import kotlinx.android.synthetic.main.item_list_content.view.*
import timber.log.Timber

class ItemAdapter(
    private val activity: ItemListActivity,
    values: List<Any>,
    private val twoPane: Boolean
) :
    KtListAdapter<Any>(values) {

    init {
        Timber.tag("ItemAdapter").i("ItemAdapter($twoPane) $values")
        clickListener = { item, v, _, _ ->
            if (item is ItemList.Item) {
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    activity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(
                        v.context,
                        ItemDetailActivity::class.java
                    ).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) STRING_TYPE else ITEM_TYPE
    }

    override fun createVH(parent: ViewGroup, viewType: Int, adapter: KtAdapter<Any>): ViewHolder<Any> {
        return when (viewType) {
            STRING_TYPE -> TextVH(parent, adapter)
            else -> ItemViewHolder.create(parent, adapter)
        }
    }

    class ItemViewHolder(view: View, adapter: KtAdapter<Any>) : ViewHolder<Any>(view, adapter) {
        private val idView: TextView = view.id_text
        private val contentView: TextView = view.content

        override fun bind(position: Int, item: Any) {
            if (item is ItemList.Item) {
                idView.text = item.id
                contentView.text = item.content
            }
        }

        companion object {
            fun create(parent: ViewGroup, adapter: KtAdapter<Any>): ItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
                return ItemViewHolder(view, adapter)
            }
        }
    }
}