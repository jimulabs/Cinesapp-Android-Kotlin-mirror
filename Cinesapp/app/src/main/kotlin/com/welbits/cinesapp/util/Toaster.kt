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

package com.welbits.cinesapp.util

import android.content.Context
import android.widget.Toast
import android.content.res.Resources
import android.support.v4.app.Fragment

/**
 * Created by Izan Rodrigo on 11/12/14.
 */

/* Methods for context */
public fun Context.toastShort(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

public fun Context.toastShort(int: Int) {
    Toast.makeText(this, messageForInt(this, int), Toast.LENGTH_SHORT).show()
}

public fun Context.toastShort(format: String, vararg args: Any?) {
    toastShort(format.format(args))
}

public fun Context.toastLong(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_LONG).show()
}

public fun Context.toastLong(int: Int) {
    Toast.makeText(this, messageForInt(this, int), Toast.LENGTH_LONG).show()
}

public fun Context.toastLong(format: String, vararg args: Any?) {
    toastLong(format.format(args))
}

/* Methods for fragment */
public fun Fragment.toastShort(string: String) {
    getActivity().toastShort(string)
}

public fun Fragment.toastShort(int: Int) {
    getActivity().toastShort(int)
}

public fun Fragment.toastShort(format: String, vararg args: Any?) {
    getActivity().toastShort(format.format(args))
}

public fun Fragment.toastLong(string: String) {
    getActivity().toastLong(string)
}

public fun Fragment.toastLong(int: Int) {
    getActivity().toastLong(int)
}

public fun Fragment.toastLong(format: String, vararg args: Any?) {
    getActivity().toastLong(format.format(args))
}

/* Utility methods */
private fun isTextResource(context: Context, resource: Int): Boolean {
    try {
        context.getResources().getText(resource)
        return true
    } catch (ex: Resources.NotFoundException) {
        return false
    }
}

private fun messageForInt(context: Context, int: Int): String {
    return when {
        isTextResource(context, int) -> context.getText(int).toString()
        else -> int.toString()
    }
}