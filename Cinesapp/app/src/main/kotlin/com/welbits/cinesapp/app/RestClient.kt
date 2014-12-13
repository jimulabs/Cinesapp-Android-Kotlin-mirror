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

package com.welbits.cinesapp.app

import com.welbits.cinesapp.model.Cinema
import retrofit.http.Query
import retrofit.http.GET
import com.welbits.cinesapp.model.KimonoResponse
import rx.Observable
import com.welbits.cinesapp.model.Movie
import com.welbits.cinesapp.model.Promo
import com.welbits.cinesapp.model.MovieDetail

/**
 * Created by Izan Rodrigo on 11/12/14.
 */

public trait RestClient {
    // Constants
    public class object {
        public val ENDPOINT_URL: String = "https://www.kimonolabs.com/api"
        public val API_KEY: String = "mqNZkSeHrANZKPU8FVD7BwP3B6Iko1T6"
        public val PARAM_API_KEY: String = "apikey"
    }

    GET("/dlt9nj0q")
    public fun getListOfCinemas(): Observable<KimonoResponse<Cinema.Response>>

    GET("/d86tpi6u")
    public fun getBillboard(Query("kimpath4") cinema: String): Observable<KimonoResponse<Movie.Response>>

    GET("/b2oeuvq2")
    public fun getPremiere(Query("kimpath4") cinema: String): Observable<KimonoResponse<Movie.Response>>

    GET("/ap7wlhye")
    public fun getEvents(Query("kimpath4") cinema: String): Observable<KimonoResponse<Movie.Response>>

    GET("/3mxieby0")
    public fun getComingSoonPremiere(): Observable<KimonoResponse<Movie.Response>>

    GET("/4a3p161k")
    public fun getPromos(Query("kimpath3") cinema: String): Observable<KimonoResponse<Promo.Response>>

    GET("/d4wlb3ag")
    public fun getMovieDetail(Query("kimpath3") movie: String): Observable<KimonoResponse<MovieDetail.Response>>
}