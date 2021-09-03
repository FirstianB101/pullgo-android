package com.harry.pullgo.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnCheckPwListener
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.repository.AppliedAcademyGroupRepository
import com.harry.pullgo.databinding.ActivityStudentMainBinding
import com.harry.pullgo.ui.applyClassroom.ApplyClassroomActivity
import com.harry.pullgo.ui.commonFragment.ChangeInfoCheckPwFragment
import com.harry.pullgo.ui.findAcademy.FindAcademyActivity
import com.harry.pullgo.ui.studentFragment.StudentChangePersonInfoFragment
import com.harry.pullgo.ui.studentFragment.StudentHomeFragmentNoAcademy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentMainActivityNoAcademy  : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private val binding by lazy{ActivityStudentMainBinding.inflate(layoutInflater)}
    lateinit var studentHomeFragment: StudentHomeFragmentNoAcademy
    lateinit var studentChangeInfoFragment: StudentChangePersonInfoFragment
    lateinit var changeInfoCheckPwFragment: ChangeInfoCheckPwFragment

    private val homeViewModel: AppliedAcademyGroupViewModel by viewModels{AppliedAcademiesViewModelFactory(AppliedAcademyGroupRepository())}
    private val changeInfoViewModel: ChangeInfoViewModel by viewModels()

    private lateinit var headerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.studentToolbar)

        initialize()
        initViewModels()
        setListeners()
    }

    private fun initViewModels(){
        changeInfoViewModel.changeStudent.observe(this){
            changeStudentInfo(changeInfoViewModel.changeStudent.value)
            LoginInfo.loginStudent = changeInfoViewModel.changeStudent.value
            headerView.findViewById<TextView>(R.id.textViewNavFullName).text="${LoginInfo.loginStudent?.account?.fullName}님"
            headerView.findViewById<TextView>(R.id.textViewNavId).text="${LoginInfo.loginStudent?.account?.username}"
            onFragmentSelected(STUDENT_MENU.HOME)
        }
    }

    private fun initialize(){
        binding.navigationViewStudent.menu.clear()
        binding.navigationViewStudent.inflateMenu(R.menu.activity_student_main_drawer_no_academy)
        binding.textViewStudentApplyOtherAcademy.visibility = View.GONE

        studentHomeFragment = StudentHomeFragmentNoAcademy()
        studentChangeInfoFragment = StudentChangePersonInfoFragment()
        changeInfoCheckPwFragment = ChangeInfoCheckPwFragment()

        supportFragmentManager.beginTransaction().replace(R.id.studentMainFragment, studentHomeFragment).commit()

        headerView = binding.navigationViewStudent.getHeaderView(0)
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
            LoginInfo.loginStudent = null
            LoginInfo.loginTeacher = null
            finish()
        }

        changeInfoCheckPwFragment.pwCheckListenerListener = object: OnCheckPwListener{
            override fun onPasswordCheck() {
                onFragmentSelected(STUDENT_MENU.CHANGE_INFO)
            }
        }
    }

    override fun onBackPressed() {
        if (binding.studentDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.studentDrawerLayout.closeDrawer(GravityCompat.START)
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
            R.id.nav_student_home -> onFragmentSelected(STUDENT_MENU.HOME)
            R.id.nav_student_change_info -> onFragmentSelected(STUDENT_MENU.CHANGE_INFO_CHECK_PW)
            R.id.nav_student_apply_classroom -> startApplyClassroomActivity()
        }
        binding.studentDrawerLayout.closeDrawer(binding.navigationViewStudent)
        return true
    }

    private fun startApplyClassroomActivity(){
        val intent = Intent(this, ApplyClassroomActivity::class.java)
        startActivity(intent)
    }

    private fun onFragmentSelected(position: STUDENT_MENU) {
        var curFragment: Fragment? = null
        curFragment = when(position){
            STUDENT_MENU.HOME -> {
                studentHomeFragment
            }
            STUDENT_MENU.CHANGE_INFO -> {
                StudentChangePersonInfoFragment()
            }
            STUDENT_MENU.CHANGE_INFO_CHECK_PW -> {
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
        transaction.replace(R.id.studentMainFragment, curFragment).addToBackStack(null).commit()
    }

    private fun startFindAcademyActivity(){
        val intent= Intent(applicationContext, FindAcademyActivity::class.java)
        intent.putExtra("calledByStudent",true)
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

    enum class STUDENT_MENU{
        HOME,
        CHANGE_INFO,
        CHANGE_INFO_CHECK_PW
    }
}