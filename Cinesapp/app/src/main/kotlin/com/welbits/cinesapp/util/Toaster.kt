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
private fun messageForInt(context: Context, int: Int): CharSequence {
    try {
        return context.getResources().getText(int)
    } catch (ex: Resources.NotFoundException) {
        return int.toString()
    }
}
