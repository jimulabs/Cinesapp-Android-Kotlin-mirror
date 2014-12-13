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

package com.welbits.cinesapp.ui

import com.welbits.cinesapp.app.BaseActivity
import com.welbits.cinesapp.model.Cinema
import com.welbits.cinesapp.model.KimonoResponse
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import com.welbits.cinesapp.R
import android.content.Context
import android.widget.TextView
import android.os.Bundle
import android.widget.ListView
import butterknife.bindView
import android.content.Intent
import kotlin.properties.Delegates
import com.welbits.cinesapp.util.Prefs
import com.welbits.cinesapp.adapter.BindAdapter
import com.welbits.cinesapp.adapter.ViewBinder
import rx.Observable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View

/**
 * Created by Izan Rodrigo on 11/12/14.
 */

public class SelectCinemaActivity : BaseActivity() {

    public class object {
        public val KEY_CHANGE_CINEMA: String = "key:changeCinema";

        public fun start(context: Context, changeCinema: Boolean) {
            val intent = Intent(context, javaClass<SelectCinemaActivity>())
            intent.putExtra(SelectCinemaActivity.KEY_CHANGE_CINEMA, changeCinema)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.labelSelectCinemaActivity)

        // If cinema was already selected start CinemaActivity
        if (!getIntent().hasExtra(KEY_CHANGE_CINEMA) && Prefs.has(this, Cinema.KEY)) {
            startActivity(Intent(this, javaClass<CinemaActivity>()))
            finish()
            return
        }

        // Otherwise retrieve cinema list from Kimono API
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, SelectCinemaFragment())
                .commit()
    }
}

public class SelectCinemaFragment : BaseRestFragment<KimonoResponse<Cinema.Response>?>() {
    private val listView: ListView by bindView(android.R.id.list)
    private var adapter: CinemasAdapter by Delegates.notNull()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_select_cinema, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureListView()
        super.startLoading()
    }

    private fun configureListView() {
        // Create adapter and attach it to the list view
        adapter = CinemasAdapter(getActivity(), R.layout.list_item_cinema)
        listView.setAdapter(adapter)

        // Save selected cinema on shared preferences and start CinemaActivity
        listView.setOnItemClickListener {(parent, view, position, id) ->
            val cinema = adapter[position];
            Prefs.getDefaults(getActivity())
                    .edit()
                    .putString(Cinema.KEY, Cinema.SERIALIZER.toJson(cinema))
                    .commit();
            startActivity(Intent(getActivity(), javaClass<CinemaActivity>()))
            getActivity().finish();
        }
    }

    override fun loadData(): Observable<KimonoResponse<Cinema.Response>?> {
        return restClient.getListOfCinemas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun handleData(response: KimonoResponse<Cinema.Response>?) {
        response?.results?.cinemas?.let { self ->
            adapter.addAll(self)
        }
    }

    private inner class CinemasAdapter(context: Context, layoutResource: Int) : BindAdapter<Cinema>(context, layoutResource) {

        override fun bindView(item: Cinema, viewBinder: ViewBinder) {
            // Name
            viewBinder.findView<TextView>(R.id.cinemaName).setText(item.name)
        }
    }
}