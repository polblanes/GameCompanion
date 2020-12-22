package com.gamecompanion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TAB CLICK LISTENER
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view);
        bottomNavigationView.selectedItemId = R.id.tab_home
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            //Go to the correct screen
            when (item.itemId) {
                R.id.tab_home -> {
                    //Go to home
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = HomeFragment()
                    fragmentTransaction.replace(R.id.fragment_container, fragment)
                    fragmentTransaction.commit()
                }
                R.id.tab_news -> {
                    //Go to news
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = NewsFragment()
                    fragmentTransaction.replace(R.id.fragment_container, fragment)
                    fragmentTransaction.commit()
                }
                R.id.tab_profile -> {
                    //Go to profile
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = ProfileFragment()
                    fragmentTransaction.replace(R.id.fragment_container, fragment)
                    fragmentTransaction.commit()
                }
            }

            true
        }
    }
}