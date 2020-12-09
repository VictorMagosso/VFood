package com.victormagosso.vfood.activity.company

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.victormagosso.vfood.R


class CompanyActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company)

        var bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu_company)
        supportFragmentManager.beginTransaction().replace(
            R.id.company_container, HomeFragment()
        ).commit()
        bottomNav.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.orders_menu -> selectedFragment = OrderFragment()
                R.id.profile_menu -> selectedFragment = ProfileFragment()
                R.id.explore_menu -> selectedFragment = HomeFragment()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.company_container, it).commit()
            }
            true
        }
    }
}

