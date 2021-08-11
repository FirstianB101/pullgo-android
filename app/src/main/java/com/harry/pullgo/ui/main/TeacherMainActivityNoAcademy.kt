package com.harry.pullgo.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.ui.findAcademy.FindAcademyActivity
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnCheckPw
import com.harry.pullgo.databinding.ActivityTeacherMainBinding
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.AppliedAcademyGroupRepository
import com.harry.pullgo.ui.applyClassroom.ApplyClassroomActivity
import com.harry.pullgo.ui.commonFragment.ChangeInfoCheckPwFragment
import com.harry.pullgo.ui.teacherFragment.TeacherChangePersonInfoFragment
import com.harry.pullgo.ui.teacherFragment.TeacherHomeFragmentNoAcademy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherMainActivityNoAcademy : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding by lazy{ActivityTeacherMainBinding.inflate(layoutInflater)}
    lateinit var teacherHomeFragment: TeacherHomeFragmentNoAcademy
    lateinit var teacherChangeInfoFragment: TeacherChangePersonInfoFragment
    lateinit var changeInfoCheckPwFragment: ChangeInfoCheckPwFragment

    private val homeViewModel: AppliedAcademyGroupViewModel by viewModels{AppliedAcademiesViewModelFactory(AppliedAcademyGroupRepository())}
    private val changeInfoViewModel: ChangeInfoViewModel by viewModels()

    private lateinit var headerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.teacherToolbar)

        initialize()
        initViewModels()
        setListeners()
    }

    private fun initialize(){
        binding.navigationViewTeacher.menu.clear()
        binding.navigationViewTeacher.inflateMenu(R.menu.activity_teacher_main_no_academy)
        binding.textViewTeacherApplyOtherAcademy.visibility = View.GONE

        teacherHomeFragment = TeacherHomeFragmentNoAcademy()
        teacherChangeInfoFragment = TeacherChangePersonInfoFragment()
        changeInfoCheckPwFragment = ChangeInfoCheckPwFragment()

        supportFragmentManager.beginTransaction().replace(R.id.teacherMainFragment, teacherHomeFragment).commit()

        headerView = binding.navigationViewTeacher.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.textViewNavFullName).text="${LoginInfo.loginTeacher?.account?.fullName}님"
        headerView.findViewById<TextView>(R.id.textViewNavId).text="${LoginInfo.loginTeacher?.account?.username}"
    }

    private fun initViewModels(){
        changeInfoViewModel.changeTeacher.observe(this){
            changeTeacherInfo(changeInfoViewModel.changeTeacher.value)
            LoginInfo.loginTeacher = changeInfoViewModel.changeTeacher.value
            headerView.findViewById<TextView>(R.id.textViewNavFullName).text="${LoginInfo.loginTeacher?.account?.fullName}님"
            headerView.findViewById<TextView>(R.id.textViewNavId).text="${LoginInfo.loginTeacher?.account?.username}"
            onFragmentSelected(TEACHER_MENU.HOME)
        }
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.student_additional_setting, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_teacher_home -> onFragmentSelected(TEACHER_MENU.HOME)
            R.id.nav_teacher_change_info -> onFragmentSelected(TEACHER_MENU.CHANGE_INFO_CHECK_PW)
            R.id.nav_teacher_apply_classroom -> startApplyClassroomActivity()
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
        curFragment = when(position){
            TEACHER_MENU.HOME -> {
                teacherHomeFragment
            }
            TEACHER_MENU.CHANGE_INFO -> {
                teacherChangeInfoFragment
            }
            TEACHER_MENU.CHANGE_INFO_CHECK_PW -> {
                changeInfoCheckPwFragment
            }
        }

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        transaction.replace(R.id.teacherMainFragment, curFragment).addToBackStack(null).commit()
    }

    private fun startFindAcademyActivity(){
        val intent = Intent(applicationContext, FindAcademyActivity::class.java)
        startActivity(intent)
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

    enum class TEACHER_MENU{
        HOME,
        CHANGE_INFO,
        CHANGE_INFO_CHECK_PW
    }
}