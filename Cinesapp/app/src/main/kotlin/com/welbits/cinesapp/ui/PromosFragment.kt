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

import android.content.Intent
import com.welbits.cinesapp.R
import android.view.View
import com.welbits.cinesapp.model.Promo
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import java.io.File
import android.os.Environment
import java.io.FileOutputStream
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import com.welbits.cinesapp.model.Cinema
import android.os.Bundle
import com.squareup.picasso.Picasso
import java.io.IOException
import android.net.Uri
import android.view.ViewGroup
import android.view.LayoutInflater
import com.welbits.cinesapp.model.KimonoResponse
import android.widget.ListView
import butterknife.bindView
import kotlin.properties.Delegates
import android.graphics.drawable.BitmapDrawable
import com.welbits.cinesapp.util.printStackTrace
import com.welbits.cinesapp.adapter.BindAdapter
import com.welbits.cinesapp.adapter.ViewBinder
import rx.Observable

/**
 * Created by Izan Rodrigo on 11/12/14.
 */

public class PromosFragment : BaseRestFragment<KimonoResponse<Promo.Response>?>() {

    private val listView: ListView by bindView(android.R.id.list)
    private var picasso: Picasso by Delegates.notNull()
    private var adapter: PromosAdapter by Delegates.notNull()
    private var cinema: Cinema by Delegates.notNull()

    public class object {
        public fun newInstance(cinema: Cinema): PromosFragment {
            val fragment = PromosFragment()
            val args = Bundle()
            args.putString(Cinema.KEY, Cinema.SERIALIZER.toJson(cinema))
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_list_without_dividers, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        picasso = app.picasso()
        configureListView()
        super.startLoading()
    }

    private fun configureListView() {
        // Create adapter and attach it to the list view
        adapter = PromosAdapter()
        listView.setAdapter(adapter)
    }

    override fun loadData(): Observable<KimonoResponse<Promo.Response>?> {
        val cinemaJson = getArguments().getString(Cinema.KEY)
        cinema = Cinema.SERIALIZER.fromJson(cinemaJson)
        return restClient.getPromos(cinema.url).asObservable()
    }

    override fun handleData(response: KimonoResponse<Promo.Response>?) {
        response?.results?.promos?.let { self ->
            adapter.addAll(self)
        }
        response?.results?.promosWithoutLink?.let { self ->
            adapter.addAll(self)
        }
    }

    protected inner class PromosAdapter() : BindAdapter<Promo>(getActivity(), R.layout.list_item_promo) {

        override fun bindView(item: Promo, viewBinder: ViewBinder) {
            // Image
            val image = viewBinder.findView<ImageView>(R.id.promoImage)
            picasso.load(item.image).fit().into(image)

            // Name
            viewBinder.findView<TextView>(R.id.promoName).setText(item.name)

            // Message
            viewBinder.findView<View>(R.id.promoMessage).setVisibility(if (item.hasLink()) View.GONE else View.VISIBLE)

            // Share button
            viewBinder.findView<Button>(R.id.shareButton).setOnClickListener(View.OnClickListener {
                val promoImage = (image.getDrawable() as BitmapDrawable).getBitmap();
                val message = getString(R.string.shareMessage, cinema.name, item.name);
                if (promoImage != null) {
                    val shareIntent = getShareImageIntent(promoImage, message);
                    if (shareIntent != null) {
                        startActivity(Intent.createChooser(shareIntent, getString(R.string.actionShare)));
                    } else {
                        startActivity(Intent.createChooser(getShareTextIntent(message), getString(R.string.actionShare)));
                    }
                } else {
                    startActivity(Intent.createChooser(getShareTextIntent(message), getString(R.string.actionShare)));
                }
            })

            // More info button
            val moreInfoButton = viewBinder.findView<Button>(R.id.moreInfoButton)
            if (item.hasLink()) {
                moreInfoButton.setVisibility(View.VISIBLE)
                moreInfoButton.setOnClickListener({
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
                    startActivity(intent);
                })
            } else {
                moreInfoButton.setVisibility(View.GONE)
            }
        }
    }
}

/* Static methods */
private fun getShareImageIntent(image: Bitmap, message: String): Intent? {
    return getShareTextIntent(message)
            .setType("image/png")
            .putExtra(Intent.EXTRA_STREAM, Uri.fromFile(saveImage(image)))
}

private fun getShareTextIntent(message: String): Intent {
    return Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, message)
}

private fun saveImage(image: Bitmap): File {
    val bytes = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val temp = File(Environment.getExternalStorageDirectory().path + File.separator + "temp.jpg")

    try {
        if (temp.exists()) temp.delete()
        if (temp.createNewFile()) {
            val fo = FileOutputStream(temp)
            fo.write(bytes.toByteArray())
            fo.flush()
            fo.close()
        }
    } catch (e: IOException) {
        printStackTrace(e)
    }

    return temp
}