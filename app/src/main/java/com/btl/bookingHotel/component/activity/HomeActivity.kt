package com.btl.bookingHotel.component.activity


import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.btl.bookingHotel.component.fragment.AccountFragment
import com.btl.bookingHotel.component.fragment.HomeFragment
import com.btl.bookinghotel.R
import com.btl.bookinghotel.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private val fragmentHome = HomeFragment()
    private val fragmentAccount = AccountFragment()
    private var activeFragment: Fragment = fragmentHome
    override fun initView() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, fragmentAccount, "2")
            .add(R.id.fragment, fragmentHome, "1").commit()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    switchFragment(fragmentHome)
                    true
                }

                R.id.bottom_account -> {
                    switchFragment(fragmentAccount)
                    true
                }

                else -> false
            }
        }
    }

    private fun switchFragment(fragment: Fragment) {
        if (fragment != activeFragment) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit()
            activeFragment.view?.isClickable = false
            fragment.view?.isClickable = true
            activeFragment = fragment
        }
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun getViewBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }
}