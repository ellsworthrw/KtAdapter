package com.diamondedge.ktadapter

import android.view.ViewGroup
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil

open class KtPagedListAdapter<T : Any>(private val cls: Class<T>, diffCallback: DiffUtil.ItemCallback<T>) :
    KtAdapter<T>() {

    private var mDiffer: AsyncPagedListDiffer<T> = AsyncPagedListDiffer(this, diffCallback)

    init {
        mDiffer.addPagedListListener { previousList, currentList ->
            onCurrentListChanged(previousList, currentList)
        }
    }

    override fun get(index: Int): T = mDiffer.getItem(index) ?: cls.newInstance()

    override fun getItemCount(): Int = mDiffer.itemCount

    override fun createVH(parent: ViewGroup, viewType: Int, adapter: KtAdapter<T>): ViewHolder<T> {
        return StringVH(parent, adapter)
    }

    /**
     * Set the new list to be displayed.
     *
     * If a list is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     * @param pagedList The new list to be displayed.
     */
    fun submitList(pagedList: PagedList<T>?) {
        mDiffer.submitList(pagedList)
    }

    /**
     * Called when the current PagedList is updated.
     *
     * This may be dispatched as part of [.submitList] if a background diff isn't
     * needed (such as when the first list is passed, or the list is cleared). In either case,
     * PagedListAdapter will simply call
     * [notifyItemRangeInserted/Removed(0, mPreviousSize)][.notifyItemRangeInserted].
     *
     * This method will *not*be called when the Adapter switches from presenting a PagedList
     * to a snapshot version of the PagedList during a diff. This means you cannot observe each
     * PagedList via this method.
     *
     * @param previousList PagedList that was previously displayed, may be null.
     * @param currentList new PagedList being displayed, may be null.
     *
     * @see .getCurrentList
     */
    fun onCurrentListChanged(previousList: PagedList<T>?, currentList: PagedList<T>?) {
    }
}