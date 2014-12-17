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

import com.welbits.cinesapp.model.Cinema
import android.os.Bundle
import com.welbits.cinesapp.model.KimonoResponse
import com.welbits.cinesapp.model.Movie
import rx.Observable

/**
 * Created by Izan Rodrigo on 11/12/14.
 */

public class PremiereFragment : BaseMoviesFragment() {

    public class object {
        public fun newInstance(cinema: Cinema): PremiereFragment {
            val fragment = PremiereFragment()
            val args = Bundle()
            args.putString(Cinema.KEY, Cinema.SERIALIZER.toJson(cinema))
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun loadData(): Observable<KimonoResponse<Movie.Response>?> {
        val cinemaJson = getArguments().getString(Cinema.KEY)
        val cinema = Cinema.SERIALIZER.fromJson(cinemaJson)
        return restClient.getPremiere(cinema.url).asObservable()
    }

    override fun handleData(response: KimonoResponse<Movie.Response>?) {
        response?.results?.movies?.let { self ->
            adapter.addAll(self)
        }
    }

}
