package com.victormagosso.vfood.activity.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.victormagosso.vfood.R
import com.victormagosso.vfood.activity.user.order.OrderFragment

class UserActivity : AppCompatActivity() {
    private var search: MaterialSearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

//        var viewModel: ItemOrderViewModel =
//            ViewModelProvider(this).get(ItemOrderViewModel::class.java)
//        viewModel.clearAllData()

        var toolbar: Toolbar = findViewById(R.id.toolbar_user)
        toolbar.title = "V-Food"
        setSupportActionBar(toolbar)

        search = findViewById(R.id.searchViewUser)
        var bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu_user)
        supportFragmentManager.beginTransaction().replace(
            R.id.user_container, HomeFragment()
        ).commit()
        bottomNav.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.orders_menu -> selectedFragment = OrderFragment()
                R.id.profile_menu -> selectedFragment = ProfileFragment()
                R.id.explore_menu -> selectedFragment = HomeFragment()
                R.id.cart_menu -> selectedFragment = CartFragment()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.user_container, it).commit()
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_user, menu)

        var item: MenuItem = menu!!.findItem(R.id.search)
        search!!.setMenuItem(item)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}
