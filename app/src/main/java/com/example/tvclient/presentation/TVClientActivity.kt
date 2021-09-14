package com.example.tvclient.presentation

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tvclient.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "TVClientActivity"

@AndroidEntryPoint
class TVClientActivity : AppCompatActivity() {
    private val viewModel: TVClientViewModel by viewModels()
    private var isWorkRunning = false
    private lateinit var workMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findNavController(R.id.nav_host_fragment).navigate(R.id.splash_fragment)
        Handler().postDelayed({
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_splashFragment_to_tv_fragment)
        }, 3000)

        val navController = findNavController(this, R.id.nav_host_fragment)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        NavigationUI.setupWithNavController(toolbar, navController)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.splash_fragment) {
                toolbar.visibility = View.GONE
                bottomNavigationView.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
                bottomNavigationView.visibility = View.VISIBLE
            }
        }

        viewModel.isWorkRuning.observe(this) {
            isWorkRunning = it
            workMenuItem.title = getString(if (isWorkRunning) R.string.work_manager_cancel else R.string.work_manager_start)
            workMenuItem.icon = getDrawable(if (isWorkRunning) R.drawable.ic_work_manager_off else R.drawable.ic_work_manager)
            Log.d(TAG, "observer isWorkRunning: $isWorkRunning")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        workMenuItem = menu!!.findItem(R.id.work_manager)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.work_manager -> {
                viewModel.startStopWorker(isWorkRunning)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}