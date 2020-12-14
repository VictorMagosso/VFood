package com.victormagosso.vfood.activity.company

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.victormagosso.vfood.R
import kotlinx.android.synthetic.main.toolbar_company.*

class CompanyActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company)
        var toolbar: Toolbar = findViewById(R.id.toolbar_company)
        toolbar.title = "V-Food - Empresa"
        setSupportActionBar(toolbar)

        var bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu_company)
        var addDish = findViewById<Button>(R.id.btnAddDish);

        supportFragmentManager.beginTransaction().replace(
            R.id.company_container, HomeFragment()
        ).commit()
        bottomNav.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.orders_menu -> selectedFragment = OrderFragment()
                R.id.profile_menu -> selectedFragment = ProfileFragment()
                R.id.explore_menu -> selectedFragment = HomeFragment()
                R.id.mydishes_menu -> selectedFragment = ProductsFragment()
                R.id.analysis_menu -> selectedFragment = AnalysisFragment()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.company_container, it).commit()
            }
            true
        }

        btnAddDish.setOnClickListener {
            startActivity(Intent(applicationContext, AddProductActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}

