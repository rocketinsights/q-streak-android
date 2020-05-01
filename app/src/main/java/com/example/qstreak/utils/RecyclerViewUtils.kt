package com.example.qstreak.utils

import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapted from https://gist.github.com/mustafasevgi/7dcb18166aaf9944e6450ec2878a646a
 */
object RecyclerViewUtils {

    /**
     * Add this [RecyclerView.OnScrollListener] to a recyclerview to be notified
     * of first completely visible item when scroll state is settling or idle.
     * @param firstVisibleItemAction is the callback function using the item's position.
     */
    fun getListenerForFirstVisibleItemPosition(firstVisibleItemAction: (Int) -> Unit): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState in listOf(
                        RecyclerView.SCROLL_STATE_SETTLING,
                        RecyclerView.SCROLL_STATE_IDLE
                    )
                ) {
                    firstVisibleItemAction(recyclerView.findFirstCompletelyVisibleItemPosition())
                }
            }
        }
    }

    private fun RecyclerView.findFirstCompletelyVisibleItemPosition(): Int {
        layoutManager?.let {
            val child: View? = it.findOneVisibleChild(
                0, it.childCount,
                completelyVisible = true,
                acceptPartiallyVisible = false
            )
            return if (child == null) RecyclerView.NO_POSITION else getChildAdapterPosition(
                child
            )
        }
        return 0
    }

    private fun RecyclerView.LayoutManager.findOneVisibleChild(
        fromIndex: Int, toIndex: Int, completelyVisible: Boolean,
        acceptPartiallyVisible: Boolean
    ): View? {
        val helper: OrientationHelper = OrientationHelper.createHorizontalHelper(this)
        val start = helper.startAfterPadding
        val end = helper.endAfterPadding
        val next = if (toIndex > fromIndex) 1 else -1
        var partiallyVisible: View? = null
        var i = fromIndex
        while (i != toIndex) {
            val child: View? = this.getChildAt(i)
            child?.let {
                val childStart = helper.getDecoratedStart(child)
                val childEnd = helper.getDecoratedEnd(child)
                if (childStart < end && childEnd > start) {
                    if (completelyVisible) {
                        if (childStart >= start && childEnd <= end) {
                            return child
                        } else if (acceptPartiallyVisible && partiallyVisible == null) {
                            partiallyVisible = child
                        }
                    } else {
                        return child
                    }
                }
                i += next
            }
        }
        return partiallyVisible
    }
}
