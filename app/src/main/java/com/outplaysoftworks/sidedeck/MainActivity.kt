package com.outplaysoftworks.sidedeck

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText

import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity(), LpCalculator.OnFragmentInteractionListener, LpLog.OnFragmentInteractionListener {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById<View>(R.id.container)
        mViewPager!!.adapter = mSectionsPagerAdapter

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            // getItem is called to instantiate the fragment for the given page.
            when (position) {
                0 -> return LpCalculator.newInstance()
                1 -> {
                    logFragment = LpLog.newInstance()
                    return logFragment
                }
                else -> return null
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return applicationContext.resources.getString(R.string.calculator)
                1 -> return applicationContext.resources.getString(R.string.duelLog)
            }//                case 2:
            //                    return "SECTION 3";
            return null
        }
    }

    override fun onBackPressed() {
        val p1name = findViewById<View>(R.id.LpCalculatorTextPlayer1Name) as EditText
        val p2name = findViewById<View>(R.id.LpCalculatorTextPlayer2Name) as EditText
        if (p1name.hasFocus()) {
            p1name.clearFocus()
        } else if (p2name.hasFocus()) {
            p2name.clearFocus()
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        /**
         * The [android.support.v4.view.PagerAdapter] that will provide
         * fragments for each of the sections. We use a
         * [FragmentPagerAdapter] derivative, which will keep every
         * loaded fragment in memory. If this becomes too memory intensive, it
         * may be best to switch to a
         * [android.support.v4.app.FragmentStatePagerAdapter].
         */
        var mSectionsPagerAdapter: SectionsPagerAdapter
        var logFragment: LpLog? = null
            private set
    }
}
