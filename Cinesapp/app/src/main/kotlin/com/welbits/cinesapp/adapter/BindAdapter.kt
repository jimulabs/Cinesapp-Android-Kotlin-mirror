/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Izan Rodrigo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.welbits.cinesapp.adapter

import android.content.Context
import android.widget.BaseAdapter
import java.util.ArrayList
import java.util.Comparator
import java.util.Collections
import android.view.View
import android.view.ViewGroup
import android.util.SparseArray
import android.widget.ArrayAdapter

/**
 * Created by Izan Rodrigo on 12/12/14.
 */

public abstract class BindArrayAdapter<T>(context: Context,
                                          private val layout: Int,
                                          items: ArrayList<T> = ArrayList()) : ArrayAdapter<T>(context, layout, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view: View = when (convertView) {
            null -> View.inflate(getContext(), layout, null)
            else -> convertView
        }
        bindView(item, ViewBinder(view))
        return view
    }

    protected abstract fun bindView(item: T, viewBinder: ViewBinder)
}

public abstract class BindAdapter<T>(context: Context,
                                     private val layout: Int,
                                     items: ArrayList<T> = ArrayList()) : AbstractAdapter<T>(context, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view: View = when (convertView) {
            null -> View.inflate(context, layout, null)
            else -> convertView
        }
        bindView(item, ViewBinder(view))
        return view
    }

    protected abstract fun bindView(item: T, viewBinder: ViewBinder)
}

public class ViewBinder(private val rootView: View) {

    private class ViewHolder : SparseArray<View>()

    private fun <T : View> find(rootView: View, viewResource: Int): T {
        var holder = rootView.getTag()
        if (holder == null) {
            holder = ViewHolder()
            rootView.setTag(holder)
        }

        val viewHolder = (holder as ViewHolder)
        var childView = viewHolder.get(viewResource);
        if (childView == null) {
            childView = rootView.findViewById(viewResource);
            viewHolder.put(viewResource, childView);
        }

        if (childView != null) {
            return childView as T
        } else {
            throw IllegalStateException("view with id = $viewResource not found")
        }
    }

    public fun <T : View> findView(viewResource: Int): T {
        return find(rootView, viewResource)
    }
}

public abstract class AbstractAdapter<T>(
        protected val context: Context,
        private val items: ArrayList<T> = ArrayList()) : BaseAdapter() {

    override fun getCount() = synchronized { items.count() }

    override fun getItem(position: Int) = synchronized { items[position] }

    override fun getItemId(position: Int) = synchronized { position.toLong() }

    public fun clear(): Unit = synchronized { notifyAfter { items.clear() } }

    public fun sort(comparator: Comparator<T>): Unit = synchronized {
        notifyAfter {
            Collections.sort(items, comparator)
        }
    }

    public fun add(item: T): Boolean = synchronized {
        notifyAfter {
            items.add(item)
        }
    }

    public fun add(index: Int, item: T): Unit = set(index, item)

    public fun addAll(collection: Collection<T>): Boolean = synchronized {
        notifyAfter {
            items.addAll(collection)
        }
    }

    public fun addAll(index: Int, collection: Collection<T>): Boolean = synchronized {
        notifyAfter {
            items.addAll(index, collection)
        }
    }

    public fun addAll(vararg list: T): Unit = synchronized {
        notifyAfter {
            items.addAll(list)
        }
    }

    public fun remove(index: Int): T = synchronized {
        notifyAfter {
            items.remove(index)
        }
    }

    public fun remove(item: T): Boolean = synchronized {
        notifyAfter {
            items.remove(item)
        }
    }

    /* Overloaded operators */
    // adapter[position]
    public fun get(index: Int): T = synchronized { getItem(index) }

    // adapter[position] = item
    public fun set(index: Int, item: T): Unit = synchronized { items.add(index, item) }

    // item in adapter
    public fun contains(item: T): Boolean = synchronized { items.contains(item) }

    /* Utility methods */
    inline fun <R> synchronized(block: () -> R): R {
        synchronized(this) {
            return block()
        }
    }

    inline fun <R> notifyAfter(block: () -> R): R {
        val result = block();
        notifyDataSetChanged()
        return result
    }
}