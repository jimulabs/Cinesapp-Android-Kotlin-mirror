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

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import android.os.Bundle
import com.google.gson.Gson

/**
 * Utility
 *
 * Created by Izan Rodrigo on 11/12/14.
 *
 * @param <T> Type of the model
 */

public fun <T> SimpleSerializer(clazz: Class<T>): SimpleSerializer<T> {
    return SimpleSerializer.WithClass(clazz)
}

public fun <T> SimpleSerializer(typeToken: TypeToken<T>): SimpleSerializer<T> {
    return SimpleSerializer.WithTypeToken(typeToken)
}

public trait SimpleSerializer<T> {
    public fun fromJson(json: String): T

    public fun toJson(obj: T): String

    public fun toPrettyJson(obj: T): String

    public fun fromBundle(bundle: Bundle, key: String): T {
        if (!bundle.containsKey(key)) throw IllegalStateException("key is not contained in the bundle")
        val json = bundle.getString(key)
        return fromJson(json)
    }

    public fun clone(obj: T): T = fromJson(toJson(obj))

    public class object {
        private val GSON: Gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()

        private val PRETTY_GSON: Gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create()
    }

    class WithClass<T>(private val clazz: Class<T>) : SimpleSerializer<T> {
        override fun fromJson(json: String): T = GSON.fromJson<T>(json, clazz)

        override fun toJson(obj: T): String = GSON.toJson(obj, clazz)

        override fun toPrettyJson(obj: T): String = PRETTY_GSON.toJson(obj, clazz)
    }

    class WithTypeToken<T>(private val typeToken: TypeToken<T>) : SimpleSerializer<T> {
        override fun fromJson(json: String): T = GSON.fromJson<T>(json, typeToken.getType())

        override fun toJson(obj: T): String = GSON.toJson(obj, typeToken.getType())

        override fun toPrettyJson(obj: T): String = PRETTY_GSON.toJson(obj, typeToken.getType())
    }
}