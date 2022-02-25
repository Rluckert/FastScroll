/*
 * Copyright 2022 Randy Webster. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.l4digital.fastscroll

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * A layout that contains and manages a [RecyclerView] with a [FastScroller].
 *
 * FastScrollView simplifies implementation by creating the RecyclerView and FastScroller,
 * and managing the FastScroller lifecycle. It's also useful when the parent ViewGroup
 * requires a single child view, for example a [SwipeRefreshLayout].
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class FastScrollView : FrameLayout {

    private val layout: Layout

    constructor(context: Context) : super(context) {
        layout = context.layout()
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        layout = context.layout(attrs)
    }

    /**
     * The current [FastScroller] for this [FastScrollView].
     */
    val fastScroller: FastScroller get() = layout.fastScroller

    /**
     * The current [RecyclerView] for this [FastScrollView].
     */
    val recyclerView: RecyclerView get() = layout.recyclerView

    /**
     * A [RecyclerView.Adapter] that implements [FastScroller.SectionIndexer]
     * to provide child views for the RecyclerView and section text for the FastScroller.
     */
    var adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>?
        get() = recyclerView.adapter
        set(adapter) {
            recyclerView.adapter = adapter
            when (adapter) {
                is FastScroller.SectionIndexer -> fastScroller.setSectionIndexer(adapter)
                null -> fastScroller.setSectionIndexer(null)
            }
        }

    /**
     * A [RecyclerView.LayoutManager] for the RecyclerView.
     */
    var layoutManager: RecyclerView.LayoutManager?
        get() = recyclerView.layoutManager
        set(layoutManager) {
            recyclerView.layoutManager = layoutManager
        }

    override fun onAttachedToWindow() = super.onAttachedToWindow().also {
        addView(recyclerView)
        recyclerView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        fastScroller.attachRecyclerView(recyclerView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            isNestedScrollingEnabled = true
        } else if (parent is SwipeRefreshLayout) {
            fastScroller.setSwipeRefreshLayout(parent as SwipeRefreshLayout)
        }
    }

    override fun onDetachedFromWindow() {
        fastScroller.detachRecyclerView()
        removeAllViews()
        super.onDetachedFromWindow()
    }

    /**
     * Set a new [FastScroller.FastScrollListener] that will listen to fast scroll events.
     *
     * @param fastScrollListener The new [FastScroller.FastScrollListener] to set, or null to set none
     */
    fun setFastScrollListener(fastScrollListener: FastScroller.FastScrollListener?) =
        fastScroller.setFastScrollListener(fastScrollListener)

    /**
     * Set the enabled state of fast scrolling.
     *
     * @param enabled True to enable fast scrolling, false otherwise
     */
    fun setFastScrollEnabled(enabled: Boolean) {
        fastScroller.isEnabled = enabled
    }

    /**
     * Hide the scrollbar when not scrolling.
     *
     * @param hideScrollbar True to hide the scrollbar, false to show
     */
    fun setHideScrollbar(hideScrollbar: Boolean) = fastScroller.setHideScrollbar(hideScrollbar)

    /**
     * Show the scroll track while scrolling.
     *
     * @param visible True to show scroll track, false to hide
     */
    fun setTrackVisible(visible: Boolean) = fastScroller.setTrackVisible(visible)

    /**
     * Set the color of the scroll track.
     *
     * @param color The color for the scroll track
     */
    fun setTrackColor(@ColorInt color: Int) = fastScroller.setTrackColor(color)

    /**
     * Set the color of the scroll handle.
     *
     * @param color The color for the scroll handle
     */
    fun setHandleColor(@ColorInt color: Int) = fastScroller.setHandleColor(color)

    /**
     * Show the section bubble while scrolling.
     *
     * @param visible True to show the bubble, false to hide
     * @param always  True to always show the bubble, false to only show on handle touch
     */
    @JvmOverloads
    fun setBubbleVisible(visible: Boolean, always: Boolean = false) = fastScroller.setBubbleVisible(visible, always)

    /**
     * Set the background color of the section bubble.
     *
     * @param color The background color for the section bubble
     */
    fun setBubbleColor(@ColorInt color: Int) = fastScroller.setBubbleColor(color)

    /**
     * Set the text color of the section bubble.
     *
     * @param color The text color for the section bubble
     */
    fun setBubbleTextColor(@ColorInt color: Int) = fastScroller.setBubbleTextColor(color)

    /**
     * Set the scaled pixel text size of the section bubble.
     *
     * @param size The scaled pixel text size for the section bubble
     */
    fun setBubbleTextSize(size: Int) = fastScroller.setBubbleTextSize(size)

    private fun Context.layout(attrs: AttributeSet? = null) = Layout(
        FastScroller(this, attrs).apply { id = R.id.fast_scroller },
        RecyclerView(this, attrs).apply { id = R.id.recycler_view }
    )

    @Suppress("UseDataClass")
    private class Layout(val fastScroller: FastScroller, val recyclerView: RecyclerView)
}
