package com.example.tvclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tvclient.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TVClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findNavController(R.id.nav_host_fragment).navigate(R.id.splash_fragment)
        Handler().postDelayed({
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_splashFragment_to_tv_fragment)
        }, 3000)

        val navController = findNavController(this, R.id.nav_host_fragment)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
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
    }
}