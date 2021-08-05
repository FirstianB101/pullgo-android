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
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.ui.findAcademy.FindAcademyActivity
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnCheckPw
import com.harry.pullgo.ui.calendar.CalendarFragment
import com.harry.pullgo.databinding.ActivityTeacherMainBinding
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.AppliedAcademyGroupRepository
import com.harry.pullgo.ui.applyClassroom.ApplyClassroomActivity
import com.harry.pullgo.ui.commonFragment.ChangeInfoCheckPwFragment
import com.harry.pullgo.ui.teacherFragment.TeacherAcceptApplyAcademyFragment
import com.harry.pullgo.ui.teacherFragment.TeacherChangePersonInfoFragment
import com.harry.pullgo.ui.teacherFragment.TeacherManageAcademyFragment
import com.harry.pullgo.ui.teacherFragment.TeacherManageClassroomFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding by lazy{ActivityTeacherMainBinding.inflate(layoutInflater)}
    lateinit var teacherChangeInfoFragment: TeacherChangePersonInfoFragment
    lateinit var changeInfoCheckPwFragment: ChangeInfoCheckPwFragment
    lateinit var calendarFragment: CalendarFragment
    lateinit var manageClassroomFragment: TeacherManageClassroomFragment
    lateinit var acceptApplyAcademyFragment: TeacherAcceptApplyAcademyFragment
    lateinit var manageAcademyFragment: TeacherManageAcademyFragment

    lateinit var homeViewModel: AppliedAcademyGroupViewModel
    lateinit var changeInfoViewModel: ChangeInfoViewModel

    private lateinit var headerView: View

    private val client by lazy{RetrofitClient.getApiService()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.teacherToolbar)

        initialize()
        initViewModels()
        setListeners()

        changeMenuIfOwner(LoginInfo.loginTeacher?.id!!)
    }

    private fun initViewModels(){
        val homeViewModelFactory = AppliedAcademiesViewModelFactory(AppliedAcademyGroupRepository())
        homeViewModel = ViewModelProvider(this,homeViewModelFactory).get(AppliedAcademyGroupViewModel::class.java)

        changeInfoViewModel = ViewModelProvider(this).get(ChangeInfoViewModel::class.java)

        changeInfoViewModel.changeTeacher.observe(this){
            changeTeacherInfo(changeInfoViewModel.changeTeacher.value)
            LoginInfo.loginTeacher = changeInfoViewModel.changeTeacher.value
            headerView.findViewById<TextView>(R.id.textViewNavFullName).text="${LoginInfo.loginTeacher?.account?.fullName}님"
            headerView.findViewById<TextView>(R.id.textViewNavId).text="${LoginInfo.loginTeacher?.account?.username}"
            onFragmentSelected(TEACHER_MENU.CALENDAR)
        }
    }

    private fun initialize(){
        teacherChangeInfoFragment = TeacherChangePersonInfoFragment()
        changeInfoCheckPwFragment = ChangeInfoCheckPwFragment()
        calendarFragment = CalendarFragment()
        manageClassroomFragment = TeacherManageClassroomFragment()
        acceptApplyAcademyFragment = TeacherAcceptApplyAcademyFragment()
        manageAcademyFragment = TeacherManageAcademyFragment()

        supportFragmentManager.beginTransaction().replace(R.id.teacherMainFragment, calendarFragment).commit()

        headerView = binding.navigationViewTeacher.getHeaderView(0)
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

        changeInfoCheckPwFragment.pwCheckListener = object: OnCheckPw{
            override fun onPasswordCheck() {
                onFragmentSelected(TEACHER_MENU.CHANGE_INFO)
            }
        }
    }

    override fun onBackPressed() {
        if (binding.teacherDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.teacherDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.student_additional_setting, menu)
        return true
    }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_teacher_change_info -> onFragmentSelected(TEACHER_MENU.CHANGE_INFO_CHECK_PW)
            R.id.nav_teacher_calendar -> onFragmentSelected(TEACHER_MENU.CALENDAR)
            R.id.nav_teacher_manage_classroom -> onFragmentSelected(TEACHER_MENU.MANAGE_CLASSROOM)
            R.id.nav_teacher_apply_classroom -> startApplyClassroomActivity()
            R.id.nav_teacher_manage_accept_academy -> onFragmentSelected(TEACHER_MENU.ACCEPT_ACADEMY)
            R.id.nav_teacher_manage_academy -> onFragmentSelected(TEACHER_MENU.MANAGE_ACADEMY)
        }
        binding.teacherDrawerLayout.closeDrawer(binding.navigationViewTeacher)
        return true
    }

    private fun startApplyClassroomActivity(){
        val intent = Intent(this, ApplyClassroomActivity::class.java)
        startActivity(intent)
    }

    private fun onFragmentSelected(position: TEACHER_MENU) {
        var curFragment: Fragment? = null
        when(position){
            TEACHER_MENU.CHANGE_INFO -> {
                curFragment = teacherChangeInfoFragment
            }
            TEACHER_MENU.CALENDAR -> {
                curFragment = calendarFragment
            }
            TEACHER_MENU.CHANGE_INFO_CHECK_PW -> {
                curFragment = changeInfoCheckPwFragment
            }
            TEACHER_MENU.MANAGE_CLASSROOM -> {
                curFragment = manageClassroomFragment
            }
            TEACHER_MENU.ACCEPT_ACADEMY -> {
                curFragment = acceptApplyAcademyFragment
            }
            TEACHER_MENU.MANAGE_ACADEMY -> {
                curFragment = manageAcademyFragment
            }
            else -> {}
        }

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        transaction.replace(R.id.teacherMainFragment, curFragment!!).addToBackStack(null).commit()
    }

    private fun startFindAcademyActivity(){
        val intent= Intent(applicationContext, FindAcademyActivity::class.java)
        startActivity(intent)
    }

    private fun changeMenuIfOwner(teacherId: Long){
        client.getOwnedAcademyByCall(teacherId).enqueue(object: Callback<List<Academy>>{
            override fun onResponse(call: Call<List<Academy>>, response: Response<List<Academy>>) {
                if(response.isSuccessful){
                    response.body().let{
                        if(it?.isNotEmpty() == true)
                            binding.navigationViewTeacher.menu.getItem(6).isVisible = true
                    }
                }
            }

            override fun onFailure(call: Call<List<Academy>>, t: Throwable) {
            }
        })
    }

    private fun changeTeacherInfo(teacher: Teacher?){
        if (teacher != null) {
            client.changeTeacherInfo(teacher.id!!,teacher).enqueue(object: Callback<Teacher> {
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

    enum class TEACHER_MENU{
        CHANGE_INFO,
        CALENDAR,
        EXAM_LIST,
        MANAGE_CLASSROOM,
        PREVIOUS_EXAM,
        CHANGE_INFO_CHECK_PW,
        APPLY_CLASSROOM,
        ACCEPT_ACADEMY,
        MANAGE_ACADEMY
    }
}