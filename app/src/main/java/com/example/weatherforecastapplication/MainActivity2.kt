package com.example.weatherforecastapplication

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.view.InitFragment
import com.example.weatherforecastapplication.view.MapsActivity
import com.example.weatherforecastapplication.view.notifications_view.AlarmFragment
import com.example.weatherforecastapplication.view.favorite_view.FavFragment
import com.example.weatherforecastapplication.view.notifications_view.NotificationFragment
import com.example.weatherforecastapplication.view.settings_view.SettingsFragment
import com.example.weatherforecastapplication.view.home_view.HomeFragment
import com.example.weatherforecastapplication.view.notifications_view.AlarmSoundFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.navigation.NavigationView
import java.util.Locale

private lateinit var drawerLayout: DrawerLayout


class MainActivity2 : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferencesManager = SharedPreferencesManager.getInstance(this)
        val savedLanguage = sharedPreferencesManager.getLanguage()
        if (!savedLanguage.isNullOrBlank()) {
            val locale = Locale(savedLanguage)
            Locale.setDefault(locale)
            val configuration = Configuration()
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
            drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            val navigationView = findViewById<NavigationView>(R.id.nav_view)
            navigationView.setNavigationItemSelectedListener(this)
            val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            if (savedInstanceState == null) {
                val fragmentToLoad = intent.getStringExtra("fragment_to_load")
                if (fragmentToLoad != null) {
                    // Handle navigation to the desired fragment based on the tag or identifier
                    val fragment = when (fragmentToLoad) {
                        "YourFragmentTag" -> InitFragment()
                        else -> null // Handle default case or invalid tag
                    }
                    if (fragment != null) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit()
                    }
                } else{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
                navigationView.setCheckedItem(R.id.nav_home)}
            }
        }
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.nav_home -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
                R.id.nav_settings -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FavFragment()).commit()
                R.id.nav_share -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AlarmSoundFragment()).commit()
                R.id.nav_about -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SettingsFragment()).commit()
                R.id.nav_notification -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NotificationFragment()).commit()
//                R.id.nav_logout -> Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }
        override fun onBackPressed() {
            super.onBackPressed()
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }

}
