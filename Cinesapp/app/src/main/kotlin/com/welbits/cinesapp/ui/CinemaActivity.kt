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

import com.welbits.cinesapp.R
import com.welbits.cinesapp.model.Cinema
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.astuetz.PagerSlidingTabStrip
import com.welbits.cinesapp.app.BaseActivity
import butterknife.bindView
import android.view.Menu
import android.view.MenuItem
import kotlin.properties.Delegates
import android.support.v4.app.Fragment
import java.util.ArrayList
import android.support.v4.app.FragmentPagerAdapter
import java.util.Collections
import android.view.ViewGroup
import android.view.View
import android.view.LayoutInflater
import com.welbits.cinesapp.util.Prefs
import com.welbits.cinesapp.app.BaseFragment

/**
 * Created by Izan Rodrigo on 11/12/14.
 */

public class CinemaActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If cinema is not selected => IllegalStateException
        if (!Prefs.has(this, Cinema.KEY)) {
            throw IllegalStateException("You have to select a cinema before loading CinemaActivity")
        }

        // Otherwise load cinema information
        val cinemaJson = Prefs.getDefaults(this).getString(Cinema.KEY, null)!!
        val cinema = Cinema.SERIALIZER.fromJson(cinemaJson)
        setTitle(getString(R.string.labelCinemaActivity, cinema.name))

        // TODO: Tablet mode
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, CinemaFragment.newInstance(cinema))
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_cinema, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.actionChangeCinema -> {
                SelectCinemaActivity.start(this, true)
                finish()
            }
            R.id.actionOpenSourceLicensesFragment -> {
                OpenSourceLicensesFragment.show(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

public class CinemaFragment : BaseFragment() {

    private val tabStrip: PagerSlidingTabStrip by bindView(R.id.tabStrip)
    private val viewPager: ViewPager by bindView(R.id.viewPager)
    private var cinema: Cinema by Delegates.notNull()

    public class object {
        public fun newInstance(cinema: Cinema): CinemaFragment {
            val fragment = CinemaFragment()
            val args = Bundle()
            args.putString(Cinema.KEY, Cinema.SERIALIZER.toJson(cinema))
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_cinema, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getBaseActivity().getSupportActionBar().setElevation(0.0f)
        cinema = Cinema.SERIALIZER.fromBundle(getArguments(), Cinema.KEY)
        configureViewPager()
    }

    private fun configureViewPager() {
        val pagerAdapter = PagerAdapter()
        pagerAdapter.addAll(
                BillboardFragment.newInstance(cinema),
                PremiereFragment.newInstance(cinema),
                EventsFragment.newInstance(cinema),
                ComingSoonFragment(),
                PromosFragment.newInstance(cinema)
        )
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount())
        viewPager.setAdapter(pagerAdapter)
        tabStrip.setViewPager(viewPager)
    }

    private inner class PagerAdapter : FragmentPagerAdapter(getFragmentManager()), PagerSlidingTabStrip.CustomTabProvider {

        // Fields
        private val fragments: List<Fragment> = ArrayList()
        private val titles: Array<String> = getResources().getStringArray(R.array.cinemaActivityTabsTitles)

        override fun getItem(position: Int) = fragments[position]

        override fun getPageTitle(position: Int) = titles[position]

        override fun getCount() = fragments.size

        public fun addAll(vararg fragments: Fragment): Boolean {
            return Collections.addAll(this.fragments, *fragments)
        }

        override fun getCustomTabView(root: ViewGroup, position: Int): View {
            return LayoutInflater.from(getActivity()).inflate(R.layout.tab_material, root, false)
        }
    }
}
