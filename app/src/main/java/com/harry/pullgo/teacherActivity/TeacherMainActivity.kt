package com.harry.pullgo.teacherActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.harry.pullgo.FindAcademyActivity
import com.harry.pullgo.R
import com.harry.pullgo.calendar.CalendarFragment
import com.harry.pullgo.databinding.ActivityTeacherMainBinding
import com.harry.pullgo.interfaces.FragmentCallback
import com.harry.pullgo.interfaces.RetrofitClient
import com.harry.pullgo.interfaces.RetrofitService
import com.harry.pullgo.objects.Academy
import com.harry.pullgo.teacherFragment.TeacherChangePersonInfoFragment
import com.harry.pullgo.teacherFragment.TeacherHomeFragmentNoAcademy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TeacherMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    FragmentCallback {
    private val binding by lazy{ActivityTeacherMainBinding.inflate(layoutInflater)}
    lateinit var teacherHomeFragment: TeacherHomeFragmentNoAcademy
    lateinit var teacherChangeInfoFragment: TeacherChangePersonInfoFragment
    lateinit var calendarFragment: CalendarFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.teacherToolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.teacherDrawerLayout,
            binding.teacherToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.teacherDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationViewTeacher.setNavigationItemSelectedListener(this)

        teacherHomeFragment = TeacherHomeFragmentNoAcademy()
        teacherChangeInfoFragment = TeacherChangePersonInfoFragment()
        calendarFragment = CalendarFragment()

        supportFragmentManager.beginTransaction().replace(R.id.teacherMainFragment, teacherHomeFragment).commit()
        supportActionBar?.title = "Home"

        val header: View = binding.navigationViewTeacher.getHeaderView(0)
        header.findViewById<TextView>(R.id.textViewNavFullName).text="${intent.getStringExtra("fullName")}님"
        header.findViewById<TextView>(R.id.textViewNavId).text=intent.getStringExtra("userName")

        changeMenuIfOwner(intent.getLongExtra("id",-1))
    }

    fun logoutButtonClicked(v: View?) {
        finish()
    }

    fun applyOtherAcademyButtonClicked(v:View?){
        startFindAcademyActivity()
    }

    override fun onBackPressed() {
        if (binding.teacherDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.teacherDrawerLayout.closeDrawer(GravityCompat.START)
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
            R.id.nav_teacher_home -> onFragmentSelected(0, null)
            R.id.nav_teacher_change_info -> onFragmentSelected(1, null)
            R.id.nav_teacher_calendar -> onFragmentSelected(2, null)
        }
        binding.teacherDrawerLayout.closeDrawer(binding.navigationViewTeacher)
        return true
    }

    override fun onFragmentSelected(position: Int, bundle: Bundle?) {
        var curFragment: Fragment? = null
        if (position == 0) {
            curFragment = teacherHomeFragment
            binding.teacherToolbar.title = "Home"
        } else if (position == 1) {
            curFragment = teacherChangeInfoFragment
            binding.teacherToolbar.title = "회원정보 변경"
        } else if (position == 2) {
            curFragment = calendarFragment
            binding.teacherToolbar.title = "일정"
        }

        supportFragmentManager.beginTransaction().replace(R.id.teacherMainFragment, curFragment!!)
            .commit()
    }

    private fun startFindAcademyActivity(){
        val intent= Intent(applicationContext, FindAcademyActivity::class.java)
        startActivity(intent)
    }

    private fun changeMenuIfOwner(teacherId: Long){
        val retrofit= RetrofitClient.getInstance()
        val service=retrofit.create(RetrofitService::class.java)
        var academy: List<Academy>? = null

        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                academy=service.getOwnedAcademy(teacherId).execute().body()
            }.await()
                if(academy!!.isEmpty()){
                    binding.navigationViewTeacher.menu.removeItem(R.id.nav_teacher_manage_academy)
                }
        }
    }
}