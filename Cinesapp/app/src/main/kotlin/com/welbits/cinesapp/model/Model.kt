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

package com.welbits.cinesapp.model

import com.google.gson.annotations.Expose
import android.text.TextUtils
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.text.WordUtils
import com.google.gson.reflect.TypeToken

/**
 * Created by Izan Rodrigo on 11/12/14.
 */

/**
 * JsonModel<T>
 */
public trait JsonModel<T> {
    public val KEY: String
    public val SERIALIZER: SimpleSerializer<T>
    public val LIST_SERIALIZER: SimpleSerializer<List<T>>
}

/**
 * Cinema
 */
public data class Cinema(Expose val name: String, Expose val url: String) {
    public data class Response(Expose public val cinemas: List<Cinema>?)


    public class object : JsonModel<Cinema> {
        override public val KEY = "key:cinema"
        override public val SERIALIZER = SimpleSerializer(javaClass<Cinema>())
        override public val LIST_SERIALIZER = SimpleSerializer(object : TypeToken<List<Cinema>>() {})
    }
}

/**
 * Movie
 */
public class Movie (
        Expose private val title: String,
        Expose public val url: String,
        Expose public val poster: String,
        Expose public val length: String,
        Expose public val assessment: String,
        Expose public val releaseDate: String
) {
    Expose public var titleFormatted: String? = null
        get() {
            if ($titleFormatted == null) {
                $titleFormatted = WordUtils.capitalizeFully(title)
            }
            return $titleFormatted
        }
        private set

    public data class Response(Expose public val movies: List<Movie>?)

    public class object : JsonModel<Movie> {
        override public val KEY = "key:movie"
        override public val SERIALIZER = SimpleSerializer(javaClass<Movie>())
        override public val LIST_SERIALIZER = SimpleSerializer(object : TypeToken<List<Movie>>() {})
    }
}

/**
 * MovieDetail
 */
public class MovieDetail(
        Expose private val title: String,
        Expose public val director: String,
        Expose public val actors: String,
        Expose public val gender: String,
        Expose public val length: String,
        Expose public val image: String,
        Expose public val synopsis: String,
        Expose public val trailer: String
) {
    Expose public var titleFormatted: String? = null
        get() {
            if ($titleFormatted == null) {
                $titleFormatted = WordUtils.capitalizeFully(title)
            }
            return $titleFormatted
        }
        private set

    public data class Response(Expose public val movie: List<MovieDetail>?)

    public class object : JsonModel<MovieDetail> {
        override public val KEY = "key:movieDetail"
        override public val SERIALIZER = SimpleSerializer(javaClass<MovieDetail>())
        override public val LIST_SERIALIZER = SimpleSerializer(object : TypeToken<List<MovieDetail>>() {})
    }
}

/**
 * Promo
 */
public data class Promo(Expose val name: String, Expose val image: String, Expose val link: String) {
    public data class Response(
            Expose public val promos: List<Promo>?,
            Expose public val promosWithoutLink: List<Promo>?
    )

    public fun hasLink(): Boolean = !TextUtils.isEmpty(link)

    public class object : JsonModel<Promo> {
        override public val KEY = "key:promo"
        override public val SERIALIZER = SimpleSerializer(javaClass<Promo>())
        override public val LIST_SERIALIZER = SimpleSerializer(object : TypeToken<List<Promo>>() {})
    }
}

/**
 * KimonoResponse<T>
 */
public data class KimonoResponse<T> (
        Expose val results: T,
        Expose val name: String?,
        Expose val count: Int?,
        Expose val frequency: String?,
        Expose val version: Int?,
        Expose SerializedName("newdata") val newData: Boolean?,
        Expose SerializedName("lastrunstatus") val lastRunStatus: String?,
        Expose SerializedName("lastsuccess") val lastSuccess: String?,
        Expose SerializedName("thisversionrun") val thisVersionRun: String?,
        Expose SerializedName("thisversionstatus") var thisVersionStatus: String?,
        Expose SerializedName("nextrun") val nextRun: String?
)
