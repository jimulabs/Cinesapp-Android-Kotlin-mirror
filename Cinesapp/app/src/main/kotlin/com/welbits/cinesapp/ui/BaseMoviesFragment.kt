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

import android.support.v4.app.Fragment
import android.os.Bundle
import com.welbits.cinesapp.R
import android.text.TextUtils
import android.view.View
import retrofit.RetrofitError
import android.content.Context
import com.welbits.cinesapp.model.Movie
import android.widget.ImageView
import android.widget.TextView
import com.welbits.cinesapp.app.App
import com.squareup.picasso.Picasso
import com.welbits.cinesapp.app.RestClient
import android.view.LayoutInflater
import android.widget.ListView
import com.welbits.izanrodrigo.emptyview.library.EmptyView
import butterknife.bindView
import android.view.ViewGroup
import kotlin.properties.Delegates
import com.welbits.cinesapp.util.printStackTrace
import com.welbits.cinesapp.adapter.BindAdapter
import com.welbits.cinesapp.adapter.ViewBinder
import rx.Observable
import com.welbits.cinesapp.model.KimonoResponse
import rx.Subscription

/**
 * Created by Izan Rodrigo on 11/12/14.
 */

public abstract class BaseMoviesFragment : BaseRestFragment<KimonoResponse<Movie.Response>?>() {

    private val listView: ListView by bindView(android.R.id.list)
    private var picasso: Picasso by Delegates.notNull()
    protected var adapter: MoviesAdapter by Delegates.notNull()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        picasso = app.picasso()
        configureListView()
        super.startLoading()
    }

    private fun configureListView() {
        // Create adapter and attach it to the list view
        adapter = MoviesAdapter(getActivity(), R.layout.list_item_movie)
        listView.setAdapter(adapter)
        listView.setOnItemClickListener {(parent, view, position, id) ->
            MovieDetailActivity.start(getActivity(), adapter[position]);
        }
    }

    protected inner class MoviesAdapter(context: Context, layoutResource: Int) :
            BindAdapter<Movie>(context, layoutResource) {

        override fun bindView(item: Movie, viewBinder: ViewBinder) {
            // Poster
            val poster = viewBinder.findView<ImageView>(R.id.moviePoster)
            picasso.load(item.poster).fit().into(poster)

            // Title
            viewBinder.findView<TextView>(R.id.movieTitle).setText(item.titleFormatted)

            // Length
            val length = viewBinder.findView<TextView>(R.id.movieLength)
            if (TextUtils.isEmpty(item.length) || item.length == "0") {
                length.setText(R.string.lengthNotAvailable)
            } else {
                length.setText(getString(R.string.movieLength, item.length))
            }

            // Assessment
            val assessment = viewBinder.findView<TextView>(R.id.movieAssessment)
            if (TextUtils.isEmpty(item.assessment)) {
                assessment.setText(R.string.assessmentNotAvailable)
            } else {
                val rating = java.lang.Double.parseDouble(item.assessment.replace(',', '.'))
                assessment.setText(getString(R.string.movieAssessment, rating))
                assessment.setVisibility(View.VISIBLE)
            }

            // Release date
            val releaseDate = viewBinder.findView<TextView>(R.id.movieReleaseDate)
            if (TextUtils.isEmpty(item.releaseDate)) {
                releaseDate.setVisibility(View.GONE)
            } else {
                releaseDate.setText(getString(R.string.movieReleaseDate, item.releaseDate))
                releaseDate.setVisibility(View.VISIBLE)
            }
        }
    }
}