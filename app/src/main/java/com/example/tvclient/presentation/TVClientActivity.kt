package com.example.tvclient.presentation

import android.app.PendingIntent
import android.content.*
import android.content.Intent.EXTRA_CHOSEN_COMPONENT
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.ShareActionProvider
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tvclient.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.lifecycleScope

private const val TAG = "TVClientActivity"

@AndroidEntryPoint
class TVClientActivity : AppCompatActivity() {
    private val viewModel: TVClientViewModel by viewModels()
    var toolbar: Toolbar? = null
    private var isWorkRunning = false
    private val requestPermissionLauncher = registerForActivityResult(RequestPermission()) { onRequestPermission(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent?.action == "android.intent.action.MAIN") {
            findNavController(R.id.nav_host_fragment).navigate(R.id.splash_fragment)
            Handler().postDelayed({
                viewModel.isLoggedIn.observe(this@TVClientActivity) { isLoggedIn ->
                    val action = if(isLoggedIn) R.id.tv_fragment_action //R.id.action_splashFragment_to_tv_fragment
                                 else R.id.action_splashFragment_fragment_to_loginFragment
                    findNavController(R.id.nav_host_fragment).navigate(action)
                }
            }, 3000)
        }

        val navController = findNavController(this, R.id.nav_host_fragment)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        NavigationUI.setupWithNavController(toolbar!!, navController)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.splash_fragment) {
                toolbar?.visibility = View.GONE
                bottomNavigationView.visibility = View.GONE
            } else {
                toolbar?.visibility = View.VISIBLE
                bottomNavigationView.visibility = View.VISIBLE
            }
        }

        viewModel.isWorkRuning.observe(this) {
            isWorkRunning = it
            Log.d(TAG, "observer isWorkRunning: $isWorkRunning")
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

//        For ShareActionProvider
//        val shareItem = menu.findItem(R.id.action_share)
//        withShareActionProvider(shareItem)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item = menu!!.findItem(R.id.work_manager)
        item.title = getString(if (isWorkRunning) R.string.work_manager_cancel else R.string.work_manager_start)
        item.icon = getDrawable(if (isWorkRunning) R.drawable.ic_work_manager_off else R.drawable.ic_work_manager)

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.work_manager -> {
                viewModel.startStopWorker(isWorkRunning)
                true
            }
            R.id.menu_settings -> {
                viewModel.showSettings(this)
                true
            }
            R.id.menu_permission -> {
                getPermission()
                true
            }
            R.id.action_share -> shareAction()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareAction() : Boolean {
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hello from TVClientApp")
            putExtra(Intent.EXTRA_TITLE, "Introducing content previews")
            type = "text/plain"
        }, "Share to..")
        startActivity(share)
        return true
    }

//    private fun withShareActionProvider(shareItem: MenuItem?) {
//        val myShareActionProvider: ShareActionProvider =
//            MenuItemCompat.getActionProvider(shareItem) as ShareActionProvider
//        val share = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, "Hello from TVClientApp")
//            putExtra(Intent.EXTRA_TITLE, "Introducing content previews")
//            type = "text/plain"
//        }
//        myShareActionProvider.setShareIntent(share)
//    }

    private fun getPermission() {
        if (!applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            onRequestPermission(isGranted = false, noFeature = true)
            return
        }

        when {
            ContextCompat.checkSelfPermission(applicationContext, NEEDED_PERMISSION) ==
                    PackageManager.PERMISSION_GRANTED -> onRequestPermission(true)

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    shouldShowRequestPermissionRationale(NEEDED_PERMISSION) -> onPermissionRationale()

            else -> requestPermissionLauncher.launch(NEEDED_PERMISSION)
        }
    }

    private fun onPermissionRationale() {
        SimpleDialog.show( this,
            titleId = R.string.dialog_permission_title,
            textId = R.string.dialog_permission_text,
            onCancelClick = { onRequestPermission(false) }
        ) {
            requestPermissionLauncher.launch(NEEDED_PERMISSION)
        }
    }

    private fun onRequestPermission(isGranted: Boolean, noFeature: Boolean = false) {
        val rootView = window.decorView.rootView

        when {
            noFeature -> Snackbar.make(rootView, "No camera", Snackbar.LENGTH_LONG).show()
            isGranted -> Snackbar.make(rootView, "Permission granted", Snackbar.LENGTH_LONG).show()
            else -> Snackbar.make(rootView, "Permission denied", Snackbar.LENGTH_LONG).show()
        }
    }
}