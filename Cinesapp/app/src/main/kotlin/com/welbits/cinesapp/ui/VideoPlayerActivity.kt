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

import com.welbits.cinesapp.app.BaseActivity
import android.content.Intent
import com.welbits.cinesapp.model.MovieDetail
import android.content.Context
import com.welbits.cinesapp.R
import android.widget.VideoView
import android.os.Bundle
import butterknife.bindView
import android.annotation.TargetApi
import android.os.Build
import android.view.View
import kotlin.properties.Delegates

/**
 * Created by Izan Rodrigo on 11/12/14.
 */

public class VideoPlayerActivity : BaseActivity() {

    private val videoView: VideoView by bindView(R.id.videoView)
    private var movieDetail: MovieDetail by Delegates.notNull()

    public class object {
        public fun start(context: Context, movieDetail: MovieDetail) {
            val intent = Intent(context, javaClass<VideoPlayerActivity>())
            intent.putExtra(MovieDetail.KEY, MovieDetail.SERIALIZER.toJson(movieDetail))
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestImmersiveMode()

        // Check movie detail argument
        if (!getIntent().hasExtra(MovieDetail.KEY)) {
            finish()
            return
        }

        // Initialize activity
        val movieDetailJson = getIntent().getStringExtra(MovieDetail.KEY)
        movieDetail = MovieDetail.SERIALIZER.fromJson(movieDetailJson)
        setContentView(R.layout.activity_video_player)

        // Configure video view
        videoView.setVideoPath(movieDetail.trailer)
        videoView.setOnCompletionListener { mp -> finish() }
        videoView.setOnPreparedListener { mp -> videoView.start() }
    }

    TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) requestImmersiveMode()
    }

    TargetApi(Build.VERSION_CODES.KITKAT)
    private fun requestImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}