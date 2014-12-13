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

import android.support.v4.app.DialogFragment
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.support.v7.app.ActionBarActivity
import android.webkit.WebView
import com.welbits.cinesapp.R
import android.widget.ProgressBar
import android.view.LayoutInflater
import android.webkit.WebViewClient
import android.graphics.Bitmap
import android.view.View
import android.content.Intent
import android.net.Uri

/**
 * Created by Izan Rodrigo on 13/12/14.
 */

public class OpenSourceLicensesFragment : DialogFragment() {

    public class object {
        private val ASSETS = "file:///android_asset/licenses/";
        private val LICENSES_FILENAME = "open_source_licenses.html";
        private val OPEN_SOURCE_LICENSES_URL = ASSETS + LICENSES_FILENAME;

        public fun show(activity: ActionBarActivity) {
            val dialog = OpenSourceLicensesFragment()
            dialog.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Dialog_MinWidth)
            dialog.show(activity.getSupportFragmentManager(), null)
        }
    }

    private var webView: WebView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog? {
        val dialog = super.onCreateDialog(savedInstanceState)

        with (dialog) {
            // Request a window without the title
            getWindow().requestFeature(Window.FEATURE_NO_TITLE)

            val customView = LayoutInflater.from(getActivity())
                    .inflate(R.layout.fragment_open_source_licenses, null)
            setContentView(customView)
            setTitle(R.string.actionOpenSourceLicenses)

            // Change dialog theme
            val whiteFrame = getResources().getDrawable(android.R.drawable.dialog_holo_light_frame)
            getWindow().setBackgroundDrawable(whiteFrame)

            // Configure web view
            webView = findViewById(R.id.openSourceLicenses_webView) as WebView
            progressBar = findViewById(R.id.openSourceLicenses_progressBar) as ProgressBar
            configureWebView()
        }

        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            webView?.loadUrl(OPEN_SOURCE_LICENSES_URL)
        } else {
            webView?.restoreState(savedInstanceState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView?.saveState(outState)
    }

    private fun configureWebView() = webView?.setWebViewClient(object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            if (url == OPEN_SOURCE_LICENSES_URL) {
                progressBar?.setVisibility(View.VISIBLE)
                webView?.setVisibility(View.GONE)
            } else {
                // Load external links in default web browser
                webView?.stopLoading()
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar?.setVisibility(View.GONE)
            webView?.setVisibility(View.VISIBLE)
        }
    })
}