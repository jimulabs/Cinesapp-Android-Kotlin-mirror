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

import android.app.Application
import android.net.ConnectivityManager
import com.squareup.picasso.Picasso
import android.content.Context
import android.app.Activity
import com.welbits.cinesapp.BuildConfig
import retrofit.RestAdapter
import retrofit.client.OkClient
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarActivity

/**
 * Created by Izan Rodrigo on 11/12/14.
 */

public class App : Application() {

    private var restClient: RestClient? = null
    private var picasso: Picasso? = null

    public class object {
        public fun get(activity: Activity): App {
            return activity.getApplication() as App
        }
    }

    public fun internetIsAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.getActiveNetworkInfo()
        return activeNetworkInfo.isConnected()
    }

    public fun picasso(): Picasso {
        if (picasso == null) {
            picasso = Picasso.Builder(this)
                    .indicatorsEnabled(BuildConfig.DEBUG)
                    .loggingEnabled(BuildConfig.DEBUG)
                    .build()
        }
        return picasso!!
    }

    public fun restClient(): RestClient {
        if (restClient == null) {
            restClient = RestAdapter.Builder()
                    .setEndpoint(RestClient.ENDPOINT_URL)
                    .setClient(OkClient())
                    .setRequestInterceptor { request ->
                        request.addQueryParam(RestClient.PARAM_API_KEY, RestClient.API_KEY)
                    }
                    .build()
                    .create(javaClass<RestClient>())
        }
        return restClient!!
    }
}

public open class BaseActivity : ActionBarActivity()

public open class BaseFragment : Fragment() {
    public fun getBaseActivity(): ActionBarActivity = getActivity() as ActionBarActivity
}
