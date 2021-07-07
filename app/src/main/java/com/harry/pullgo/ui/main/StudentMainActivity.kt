package com.harry.pullgo.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.harry.pullgo.ui.studentFragment.StudentChangePersonInfoFragment
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.harry.pullgo.ui.findAcademy.FindAcademyActivity
import com.harry.pullgo.R
import com.harry.pullgo.ui.calendar.CalendarFragment
import com.harry.pullgo.databinding.ActivityStudentMainBinding
import com.harry.pullgo.data.api.FragmentCallback
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.ui.studentFragment.StudentExamHistoryFragment
import com.harry.pullgo.ui.studentFragment.StudentExamListFragment
import com.harry.pullgo.ui.studentFragment.StudentHomeFragmentNoAcademy

class StudentMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    FragmentCallback {
    private val binding by lazy{ActivityStudentMainBinding.inflate(layoutInflater)}
    lateinit var studentStudentHomeFragment: StudentHomeFragmentNoAcademy
    lateinit var studentStudentChangeInfoFragment: StudentChangePersonInfoFragment
    lateinit var calendarFragment: CalendarFragment
    lateinit var studentExamListFragment: StudentExamListFragment
    lateinit var studentExamHistroyFragment: StudentExamHistoryFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.studentToolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.studentDrawerLayout,
            binding.studentToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.studentDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationViewStudent.setNavigationItemSelectedListener(this)

        studentStudentHomeFragment = StudentHomeFragmentNoAcademy()
        studentStudentChangeInfoFragment =
            StudentChangePersonInfoFragment()
        calendarFragment = CalendarFragment()
        studentExamListFragment= StudentExamListFragment()
        studentExamHistroyFragment= StudentExamHistoryFragment()

        supportFragmentManager.beginTransaction().replace(R.id.studentMainFragment, studentStudentHomeFragment).commit()
        supportActionBar?.title = "Home"

        val header: View = binding.navigationViewStudent.getHeaderView(0)
        header.findViewById<TextView>(R.id.textViewNavFullName).text="${intent.getStringExtra("fullName")}님"
        header.findViewById<TextView>(R.id.textViewNavId).text=intent.getStringExtra("id")
    }

    fun logoutButtonClicked(v: View?) {
        LoginInfo.loginStudent=null
        LoginInfo.loginTeacher=null
        finish()
    }

    fun applyOtherAcademyButtonClicked(v:View?){
        startFindAcademyActivity()
    }

    override fun onBackPressed() {
        if (binding.studentDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.studentDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.student_additional_setting, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_student_home -> onFragmentSelected(0, null)
            R.id.nav_student_change_info -> onFragmentSelected(1, null)
            R.id.nav_student_calendar -> onFragmentSelected(2, null)
            R.id.nav_student_exam_list -> onFragmentSelected(3,null)
            R.id.nav_student_exam_history -> onFragmentSelected(4,null)
        }
        binding.studentDrawerLayout.closeDrawer(binding.navigationViewStudent)
        return true
    }

    override fun onFragmentSelected(position: Int, bundle: Bundle?) {
        var curFragment: Fragment? = null
        if (position == 0) {
            curFragment = studentStudentHomeFragment
            binding.studentToolbar.title = "Home"
        } else if (position == 1) {
            curFragment = studentStudentChangeInfoFragment
            binding.studentToolbar.title = "회원정보 변경"
        } else if (position == 2) {
            curFragment = calendarFragment
            binding.studentToolbar.title = "일정"
        }else if(position==3){
            curFragment=studentExamListFragment
            binding.studentToolbar.title="시험 목록"
        }else if(position==4){
            curFragment=studentExamHistroyFragment
            binding.studentToolbar.title="오답 노트"
        }
        supportFragmentManager.beginTransaction().replace(R.id.studentMainFragment, curFragment!!)
            .commit()
    }

    private fun startFindAcademyActivity(){
        val intent= Intent(applicationContext, FindAcademyActivity::class.java)
        startActivity(intent)
    }
}