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

import rx.Observable
import com.welbits.cinesapp.util.printStackTrace
import com.welbits.cinesapp.R
import retrofit.RetrofitError
import com.welbits.izanrodrigo.emptyview.library.EmptyView
import butterknife.bindView
import android.os.Bundle
import com.welbits.cinesapp.app.App
import com.welbits.cinesapp.app.RestClient
import kotlin.properties.Delegates
import rx.Subscription
import com.welbits.cinesapp.app.BaseFragment

/**
 * Created by Izan Rodrigo on 13/12/14.
 */

public abstract class BaseRestFragment<T> : BaseFragment() {

    private val emptyView: EmptyView by bindView(android.R.id.empty)
    protected var restClient: RestClient by Delegates.notNull()
    protected var app: App by Delegates.notNull()
    private var subscription: Subscription? = null

    override fun onDestroy() {
        super.onDestroy()
        subscription?.unsubscribe()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        app = App.get(getActivity())
        restClient = app.restClient()
    }

    protected fun startLoading() {
        emptyView.retry(R.string.retry, Runnable { loadData() })
        emptyView.startLoading()
        if (app.internetIsAvailable()) {
            subscription = loadData().subscribe({ response ->
                handleData(response)
                emptyView.successLoading()
            }, { error -> handleError(error) })
        } else {
            emptyView.error(R.string.networkError)
            emptyView.errorLoading()
        }
    }

    // Abstract functions
    protected abstract fun loadData(): Observable<T>
    protected abstract fun handleData(response: T)

    // Error handling
    protected fun handleError(throwable: Throwable?) {
        printStackTrace(throwable)
        emptyView.error(R.string.genericError)
        if (throwable != null && throwable is RetrofitError) {
            if (throwable.getResponse()?.getStatus() == 400) {
                emptyView.empty(R.string.notFound)
                emptyView.displayEmpty()
                return
            }
        }
        emptyView.errorLoading()
    }

}