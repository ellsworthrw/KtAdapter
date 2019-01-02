package com.diamondedge.ktadapter.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.diamondedge.ktadapter.KtAdapter

import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(item_list)
    }

    private fun setupRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        recyclerView.adapter = ItemAdapter(this, ItemList.ITEMS, twoPane)
    }

    class ItemAdapter(private val parentActivity: ItemListActivity,
                      private val values: List<ItemList.Item>,
                      private val twoPane: Boolean) :
            KtAdapter<Any>() {

        init {
            Log.i("ItemAdapter", "ItemAdapter($twoPane) $values")
            clickListener = { v, vh, adapter ->
                val item = adapter[vh.adapterPosition] as ItemList.Item
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun getItemCount() = values.size

        override fun get(index: Int): ItemList.Item {
            return values[index]
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) STRING_TYPE else ITEM_TYPE
        }

        override fun createVH(parent: ViewGroup, viewType: Int, adapter: KtAdapter<Any>):  ViewHolder<Any> {
            return when (viewType) {
                STRING_TYPE -> StringVH.create(parent, adapter)
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

        class StringVH(val textView: TextView, adapter: KtAdapter<Any>) : KtAdapter.ViewHolder<Any>(textView, adapter) {

            override fun bind(position: Int, item: Any) {
                textView.text = item.toString()
            }

            companion object {
                fun create(parent: ViewGroup, adapter: KtAdapter<Any>) : StringVH {
                    val textView = TextView(parent.context)
                    textView.setTextAppearance(android.R.attr.textAppearanceListItem)
                    textView.isFocusable = true
                    textView.isFocusableInTouchMode = true
                    return StringVH(textView, adapter)
                }
            }
        }

        companion object {
            private const val STRING_TYPE = 1
            private const val ITEM_TYPE = 2
        }

    }
}
