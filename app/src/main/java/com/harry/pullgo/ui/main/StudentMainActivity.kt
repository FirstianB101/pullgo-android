package com.harry.pullgo.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.harry.pullgo.ui.studentFragment.StudentChangePersonInfoFragment
import android.os.Bundle
import androidx.core.view.GravityCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.ui.findAcademy.FindAcademyActivity
import com.harry.pullgo.R
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.ui.calendar.CalendarFragment
import com.harry.pullgo.databinding.ActivityStudentMainBinding
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.AppliedAcademyGroupRepository
import com.harry.pullgo.ui.studentFragment.StudentExamHistoryFragment
import com.harry.pullgo.ui.studentFragment.StudentExamListFragment
import com.harry.pullgo.ui.studentFragment.StudentHomeFragmentNoAcademy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private val binding by lazy{ActivityStudentMainBinding.inflate(layoutInflater)}
    lateinit var studentHomeFragment: StudentHomeFragmentNoAcademy
    lateinit var studentChangeInfoFragment: StudentChangePersonInfoFragment
    lateinit var calendarFragment: CalendarFragment
    lateinit var studentExamListFragment: StudentExamListFragment
    lateinit var studentExamHistoryFragment: StudentExamHistoryFragment

    lateinit var homeViewModel: AppliedAcademyGroupViewModel
    lateinit var changeInfoViewModel: ChangeInfoViewModel

    private lateinit var headerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.studentToolbar)

        initViewModels()
        setListeners()

        initialize()
    }

    private fun initViewModels(){
        val homeViewModelFactory = AppliedAcademiesViewModelFactory(AppliedAcademyGroupRepository())
        homeViewModel = ViewModelProvider(this,homeViewModelFactory).get(AppliedAcademyGroupViewModel::class.java)

        changeInfoViewModel = ViewModelProvider(this).get(ChangeInfoViewModel::class.java)

        headerView = binding.navigationViewStudent.getHeaderView(0)

        changeInfoViewModel.changeStudent.observe(this){
            changeStudentInfo(changeInfoViewModel.changeStudent.value)
            LoginInfo.loginStudent = changeInfoViewModel.changeStudent.value
            headerView.findViewById<TextView>(R.id.textViewNavFullName).text="${LoginInfo.loginStudent?.account?.fullName}님"
            headerView.findViewById<TextView>(R.id.textViewNavId).text="${LoginInfo.loginStudent?.account?.username}"
            onFragmentSelected(0)
        }
    }

    private fun initialize(){
        studentHomeFragment = StudentHomeFragmentNoAcademy()
        studentChangeInfoFragment = StudentChangePersonInfoFragment()
        calendarFragment = CalendarFragment()
        studentExamListFragment= StudentExamListFragment()
        studentExamHistoryFragment= StudentExamHistoryFragment()

        supportFragmentManager.beginTransaction().replace(R.id.studentMainFragment, studentHomeFragment).commit()
        supportActionBar?.title = "Home"

        headerView.findViewById<TextView>(R.id.textViewNavFullName).text="${LoginInfo.loginStudent?.account?.fullName}님"
        headerView.findViewById<TextView>(R.id.textViewNavId).text="${LoginInfo.loginStudent?.account?.username}"
    }

    private fun setListeners(){
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

        binding.textViewStudentApplyOtherAcademy.setOnClickListener {
            startFindAcademyActivity()
        }

        binding.textViewStudentLogout.setOnClickListener {
            LoginInfo.loginStudent=null
            LoginInfo.loginTeacher=null
            finish()
        }
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
            R.id.nav_student_home -> onFragmentSelected(0)
            R.id.nav_student_change_info -> onFragmentSelected(1)
            R.id.nav_student_calendar -> onFragmentSelected(2)
            R.id.nav_student_exam_list -> onFragmentSelected(3)
            R.id.nav_student_exam_history -> onFragmentSelected(4)
        }
        binding.studentDrawerLayout.closeDrawer(binding.navigationViewStudent)
        return true
    }

    private fun onFragmentSelected(position: Int) {
        var curFragment: Fragment? = null
        if (position == 0) {
            curFragment = studentHomeFragment
            binding.studentToolbar.title = "Home"
        } else if (position == 1) {
            studentChangeInfoFragment = StudentChangePersonInfoFragment()
            curFragment = studentChangeInfoFragment
            binding.studentToolbar.title = "회원정보 변경"
        } else if (position == 2) {
            curFragment = calendarFragment
            binding.studentToolbar.title = "일정"
        }else if(position==3){
            curFragment=studentExamListFragment
            binding.studentToolbar.title="시험 목록"
        }else if(position==4){
            curFragment=studentExamHistoryFragment
            binding.studentToolbar.title="오답 노트"
        }
        supportFragmentManager.beginTransaction().replace(R.id.studentMainFragment, curFragment!!)
            .commit()
    }

    private fun startFindAcademyActivity(){
        val intent= Intent(applicationContext, FindAcademyActivity::class.java)
        startActivity(intent)
    }

    private fun changeStudentInfo(student: Student?){
        if (student != null) {
            val service= RetrofitClient.getApiService()

            service.changeStudentInfo(student.id!!,student).enqueue(object: Callback<Student> {
                override fun onResponse(call: Call<Student>, response: Response<Student>) {
                    if(response.isSuccessful){
                        Snackbar.make(binding.root,"계정 정보가 수정되었습니다",Snackbar.LENGTH_SHORT).show()
                    }else{
                        Snackbar.make(binding.root,"계정 정보 수정에 실패했습니다",Snackbar.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Student>, t: Throwable) {
                    Snackbar.make(binding.root,"서버와 연결에 실패하였습니다",Snackbar.LENGTH_SHORT).show()
                }
            })
        }
    }
}