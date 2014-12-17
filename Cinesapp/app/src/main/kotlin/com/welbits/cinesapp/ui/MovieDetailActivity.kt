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

import com.squareup.picasso.Picasso
import com.welbits.cinesapp.model.MovieDetail
import com.welbits.cinesapp.R
import android.widget.TextView
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import com.melnykov.fab.ObservableScrollView
import com.melnykov.fab.FloatingActionButton
import com.welbits.cinesapp.app.BaseActivity
import butterknife.bindView
import com.welbits.cinesapp.model.Movie
import android.content.Context
import android.content.Intent
import android.os.Bundle
import rx.schedulers.Schedulers
import rx.android.schedulers.AndroidSchedulers
import com.welbits.cinesapp.model.KimonoResponse
import kotlin.properties.Delegates
import com.welbits.cinesapp.util.toastShort
import rx.Observable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View

/**
 * Created by Izan Rodrigo on 11/12/14.
 */

public class MovieDetailActivity : BaseActivity() {

    public class object {
        public fun start(context: Context, movie: Movie) {
            val intent = Intent(context, javaClass<MovieDetailActivity>())
            intent.putExtra(Movie.KEY, Movie.SERIALIZER.toJson(movie))
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If movie is not in the extras => IllegalStateException
        if (!getIntent().hasExtra(Movie.KEY)) {
            throw IllegalStateException("Movie is not in the extras bundle")
        }

        // Otherwise load movie detail information
        val movieJson = getIntent().getStringExtra(Movie.KEY)
        val movie = Movie.SERIALIZER.fromJson(movieJson)

        // TODO: Tablet mode
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, MovieDetailFragment.newInstance(movie))
                .commit()
    }
}

public class MovieDetailFragment : BaseRestFragment<KimonoResponse<MovieDetail.Response>?>() {

    private val trailerFAB: FloatingActionButton by bindView(R.id.trailerFAB)
    private val scrollView: ObservableScrollView by bindView(R.id.scrollView)
    private val movieDirector: TextView by bindView(R.id.movieDirector)
    private val movieSynopsis: TextView by bindView(R.id.movieSynopsis)
    private val moviePoster: ImageView by bindView(R.id.moviePoster)
    private val movieActors: TextView by bindView(R.id.movieActors)
    private val movieGender: TextView by bindView(R.id.movieGender)
    private val movieLength: TextView by bindView(R.id.movieLength)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val title: TextView by bindView(R.id.title)
    private var picasso: Picasso by Delegates.notNull()
    private var movieDetail: MovieDetail by Delegates.notNull()
    private var movie: Movie by Delegates.notNull()

    public class object {
        public fun newInstance(movie: Movie): MovieDetailFragment {
            val fragment = MovieDetailFragment()
            val args = Bundle()
            args.putString(Movie.KEY, Movie.SERIALIZER.toJson(movie))
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_movie_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        movie = Movie.SERIALIZER.fromBundle(getArguments(), Movie.KEY)
        picasso = app.picasso()
        getBaseActivity().setSupportActionBar(toolbar)
        title.setText(movie.titleFormatted)
        configureFAB()
        super.startLoading()
    }

    private fun configureFAB() {
        trailerFAB.attachToScrollView(scrollView)
        trailerFAB.setOnLongClickListener { toastShort(R.string.playTrailer); true }
        trailerFAB.setOnClickListener { VideoPlayerActivity.start(getActivity(), movieDetail) }
    }

    override fun loadData(): Observable<KimonoResponse<MovieDetail.Response>?> {
        return restClient.getMovieDetail(movie.url).asObservable()
    }

    override fun handleData(response: KimonoResponse<MovieDetail.Response>?) {
        response?.results?.movie?.get(0)?.let { self ->
            this.movieDetail = self
            picasso.load(self.image).resizeDimen(R.dimen.posterDetailWidth, R.dimen.posterDetailHeight).into(moviePoster)
            movieDirector.setText(self.director)
            movieActors.setText(self.actors)
            movieGender.setText(self.gender)
            movieLength.setText(getString(R.string.movieLength, self.length))
            movieSynopsis.setText(self.synopsis)
        }
    }
}
