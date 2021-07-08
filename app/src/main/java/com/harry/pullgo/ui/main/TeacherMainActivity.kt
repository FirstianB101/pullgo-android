package com.harry.pullgo.ui.main

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
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.ui.findAcademy.FindAcademyActivity
import com.harry.pullgo.R
import com.harry.pullgo.ui.calendar.CalendarFragment
import com.harry.pullgo.databinding.ActivityTeacherMainBinding
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.AppliedAcademyGroupRepository
import com.harry.pullgo.ui.teacherFragment.TeacherChangePersonInfoFragment
import com.harry.pullgo.ui.teacherFragment.TeacherHomeFragmentNoAcademy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding by lazy{ActivityTeacherMainBinding.inflate(layoutInflater)}
    lateinit var teacherHomeFragment: TeacherHomeFragmentNoAcademy
    lateinit var teacherChangeInfoFragment: TeacherChangePersonInfoFragment
    lateinit var calendarFragment: CalendarFragment

    lateinit var homeViewModel: AppliedAcademyGroupViewModel
    lateinit var changeInfoViewModel: ChangeInfoViewModel

    private lateinit var headerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.teacherToolbar)

        initViewModels()
        setListeners()

        initialize()

        changeMenuIfOwner(LoginInfo.loginTeacher?.id!!)
    }

    private fun initViewModels(){
        val homeViewModelFactory = AppliedAcademiesViewModelFactory(AppliedAcademyGroupRepository())
        homeViewModel = ViewModelProvider(this,homeViewModelFactory).get(AppliedAcademyGroupViewModel::class.java)

        changeInfoViewModel = ViewModelProvider(this).get(ChangeInfoViewModel::class.java)

        headerView = binding.navigationViewTeacher.getHeaderView(0)

        changeInfoViewModel.changeTeacher.observe(this){
            changeTeacherInfo(changeInfoViewModel.changeTeacher.value)
            LoginInfo.loginTeacher = changeInfoViewModel.changeTeacher.value
            headerView.findViewById<TextView>(R.id.textViewNavFullName).text="${LoginInfo.loginTeacher?.account?.fullName}님"
            headerView.findViewById<TextView>(R.id.textViewNavId).text="${LoginInfo.loginTeacher?.account?.username}"
            onFragmentSelected(0)
        }
    }

    private fun initialize(){
        teacherHomeFragment = TeacherHomeFragmentNoAcademy()
        teacherChangeInfoFragment = TeacherChangePersonInfoFragment()
        calendarFragment = CalendarFragment()

        supportFragmentManager.beginTransaction().replace(R.id.teacherMainFragment, teacherHomeFragment).commit()
        supportActionBar?.title = "Home"

        headerView.findViewById<TextView>(R.id.textViewNavFullName).text="${LoginInfo.loginTeacher?.account?.fullName}님"
        headerView.findViewById<TextView>(R.id.textViewNavId).text="${LoginInfo.loginTeacher?.account?.username}"
    }

    private fun setListeners(){
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

        binding.textViewTeacherApplyOtherAcademy.setOnClickListener {
            startFindAcademyActivity()
        }

        binding.textViewTeacherLogout.setOnClickListener {
            LoginInfo.loginStudent=null
            LoginInfo.loginTeacher=null
            finish()
        }
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
            R.id.nav_teacher_home -> onFragmentSelected(0)
            R.id.nav_teacher_change_info -> onFragmentSelected(1)
            R.id.nav_teacher_calendar -> onFragmentSelected(2)
        }
        binding.teacherDrawerLayout.closeDrawer(binding.navigationViewTeacher)
        return true
    }

    private fun onFragmentSelected(position: Int) {
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
        val service= RetrofitClient.getApiService()
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

    private fun changeTeacherInfo(teacher: Teacher?){
        if (teacher != null) {
            val service= RetrofitClient.getApiService()

            service.changeTeacherInfo(teacher.id!!,teacher).enqueue(object: Callback<Teacher> {
                override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                    if(response.isSuccessful){
                        Snackbar.make(binding.root,"계정 정보가 수정되었습니다", Snackbar.LENGTH_SHORT).show()
                    }else{
                        Snackbar.make(binding.root,"계정 정보 수정에 실패했습니다", Snackbar.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Teacher>, t: Throwable) {
                    Snackbar.make(binding.root,"서버와 연결에 실패하였습니다", Snackbar.LENGTH_SHORT).show()
                }
            })
        }
    }
}