package com.example.weatherforecastapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.weatherforecastapplication.view.FavFragment
import com.example.weatherforecastapplication.view.NotificationFragment
import com.google.android.material.navigation.NavigationView

private lateinit var drawerLayout: DrawerLayout

class MainActivity2 : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener{
    override fun onCreate(savedInstanceState: Bundle?) {
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
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
                navigationView.setCheckedItem(R.id.nav_home)
            }
        }
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.nav_home -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
                R.id.nav_settings -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FavFragment()).commit()
                R.id.nav_share -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, NotificationFragment()).commit()
                R.id.nav_about -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, NotificationFragment()).commit()
                R.id.nav_logout -> Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show()
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
