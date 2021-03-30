package com.harry.pullgo

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.harry.pullgo.FragmentCallback
import androidx.drawerlayout.widget.DrawerLayout
import com.harry.pullgo.ui.home.HomeFragment
import com.harry.pullgo.ui.ChangePersonInfo.ChangePersonInfoFragment
import com.harry.pullgo.CalendarFragment
import android.os.Bundle
import com.harry.pullgo.R
import android.view.View
import androidx.core.view.GravityCompat
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.harry.pullgo.databinding.ActivityStudentMainBinding
import com.harry.pullgo.ui.home.HomeFragmentNoAcademy

class StudentMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    FragmentCallback {
    private val binding by lazy{ActivityStudentMainBinding.inflate(layoutInflater)}
    lateinit var studentHomeFragment: HomeFragmentNoAcademy
    lateinit var studentChangeInfoFragment: ChangePersonInfoFragment
    lateinit var calendarFragment: CalendarFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.studentToolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.studentToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationViewStudent.setNavigationItemSelectedListener(this)
        studentHomeFragment = HomeFragmentNoAcademy()
        studentChangeInfoFragment = ChangePersonInfoFragment()
        calendarFragment = CalendarFragment()
        supportFragmentManager.beginTransaction().replace(R.id.studentMainFragment, studentHomeFragment).commit()
        supportActionBar?.title = "Home"
    }

    fun logoutButtonClicked(v: View?) {
        finish()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.student_additional_setting, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_student_home -> onFragmentSelected(0, null)
            R.id.nav_student_change_info -> onFragmentSelected(1, null)
            R.id.nav_calendar -> onFragmentSelected(2, null)
        }
        binding.drawerLayout.closeDrawer(binding.navigationViewStudent)
        return true
    }

    override fun onFragmentSelected(position: Int, bundle: Bundle?) {
        var curFragment: Fragment? = null
        if (position == 0) {
            curFragment = studentHomeFragment
            binding.studentToolbar.title = "Home"
        } else if (position == 1) {
            curFragment = studentChangeInfoFragment
            binding.studentToolbar.title = "회원정보 변경"
        } else if (position == 2) {
            curFragment = calendarFragment
            binding.studentToolbar.title = "일정"
        }
        supportFragmentManager.beginTransaction().replace(R.id.studentMainFragment, curFragment!!)
            .commit()
    }
}