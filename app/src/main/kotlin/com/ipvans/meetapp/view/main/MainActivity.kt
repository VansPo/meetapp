package com.ipvans.meetapp.view.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import com.ipvans.meetapp.R
import com.ipvans.meetapp.view.create.CreateActivity
import com.ipvans.meetapp.view.events.EventListFragment
import com.ipvans.meetapp.view.widget.BottomNavigator
import java.io.Serializable

class MainActivity : AppCompatActivity(), ToolbarProvider {

    val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) as Toolbar }
    val container: ViewGroup by lazy { findViewById(R.id.container) as ViewGroup }
    val navigator by lazy { findViewById(R.id.navigator) as BottomNavigator }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBottomNavigator()

        savedInstanceState?.let {
            if (navigator.selectButton(it.getSerializable("screen")).not())
                showScreen(Screen.HOME)
        } ?: showScreen(Screen.HOME)
    }

    fun showScreen(screen: Screen) = navigator.callButtonWithTag(screen)

    private fun initBottomNavigator() {
        navigator.addButton(R.drawable.navigator_home_selector, tag = Screen.HOME) { showHomeScreen() }
                .addButton(R.drawable.navigator_explore_selector, tag = Screen.EXPLORE) { showExploreScreen() }
                .addButton(R.drawable.navigator_create_selector, tag = Screen.CREATE, shouldSelect =  false) { showCreateScreen() }
                .addButton(R.drawable.navigator_map_selector, tag = Screen.MAP) { showHomeScreen() }
                .addButton(R.drawable.navigator_account_selector, tag = Screen.ACCOUNT) { showHomeScreen() }
    }

    private fun showHomeScreen() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, EventListFragment.create(EventListFragment.MODE_HOME))
                .commit()
    }

    private fun showExploreScreen() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, EventListFragment.create(EventListFragment.MODE_EXPLORE))
                .commit()
    }

    private fun showCreateScreen() {
        startActivity(Intent(this, CreateActivity::class.java))
    }

    private fun showMapScreen() {

    }

    private fun showAccountScreen() {

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable("screen", navigator.selectedView?.tag as Serializable)
    }

    override fun provideToolbar() = toolbar
}