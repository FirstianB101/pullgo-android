package com.harry.pullgo.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.api.OnCheckPwListener
import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.ActivityTeacherMainBinding
import com.harry.pullgo.ui.applyClassroom.ApplyClassroomActivity
import com.harry.pullgo.ui.calendar.CalendarFragment
import com.harry.pullgo.ui.commonFragment.ChangeInfoCheckPwFragment
import com.harry.pullgo.ui.commonFragment.ManageRequestFragment
import com.harry.pullgo.ui.findAcademy.FindAcademyActivity
import com.harry.pullgo.ui.teacherFragment.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class TeacherMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding by lazy{ActivityTeacherMainBinding.inflate(layoutInflater)}
    lateinit var changeInfoCheckPwFragment: ChangeInfoCheckPwFragment
    lateinit var calendarFragment: CalendarFragment
    lateinit var manageClassroomFragment: TeacherManageClassroomFragment
    lateinit var acceptApplyAcademyFragment: TeacherAcceptApplyAcademyFragment
    lateinit var manageAcademyFragment: TeacherManageAcademyFragment
    lateinit var teacherHomeFragment: TeacherHomeFragmentNoAcademy

    @Inject
    lateinit var service: PullgoService

    @Inject
    lateinit var app: PullgoApplication

    private val changeInfoViewModel: ChangeInfoViewModel by viewModels()

    private lateinit var headerView: View
    private var curPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.teacherToolbar)

        initialize()
        initViewModels()
        setListeners()
    }
 
    private fun initViewModels(){
        changeInfoViewModel.changeTeacher.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    app.loginUser.teacher = it.data
                    headerView.findViewById<TextView>(R.id.textViewNavFullName).text = "${it.data?.account?.fullName}님"
                    headerView.findViewById<TextView>(R.id.textViewNavId).text = "${it.data?.account?.username}"

                    Toast.makeText(this,"회원정보가 수정되었습니다",Toast.LENGTH_SHORT).show()
                    onFragmentSelected(CALENDAR)
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(this,"정보를 수정하지 못했습니다(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initialize(){
        changeInfoCheckPwFragment = ChangeInfoCheckPwFragment()

        if(intent.getBooleanExtra("appliedAcademyExist",false)){
            calendarFragment = CalendarFragment()
            manageClassroomFragment = TeacherManageClassroomFragment()
            acceptApplyAcademyFragment = TeacherAcceptApplyAcademyFragment()
            manageAcademyFragment = TeacherManageAcademyFragment()

            supportFragmentManager.beginTransaction().replace(R.id.teacherMainFragment, calendarFragment).commit()
            curPosition = CALENDAR

            binding.navigationViewTeacher.menu.clear()
            binding.navigationViewTeacher.inflateMenu(R.menu.activity_teacher_main_drawer)
            binding.textViewTeacherApplyOtherAcademy.visibility = View.VISIBLE
            changeMenuIfOwner(app.loginUser.teacher?.id!!)
        }else{
            teacherHomeFragment = TeacherHomeFragmentNoAcademy()

            supportFragmentManager.beginTransaction().replace(R.id.teacherMainFragment, teacherHomeFragment).commit()
            curPosition = HOME
        }

        headerView = binding.navigationViewTeacher.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.textViewNavFullName).text="${app.loginUser.teacher?.account?.fullName}님"
        headerView.findViewById<TextView>(R.id.textViewNavId).text="${app.loginUser.teacher?.account?.username}"
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
            app.loginUser.student = null
            app.loginUser.teacher = null
            app.loginUser.token = null
            finish()
        }

        changeInfoCheckPwFragment.pwCheckListenerListener = object: OnCheckPwListener{
            override fun onPasswordCheck() {
                onFragmentSelected(CHANGE_INFO)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_teacher_change_info -> onFragmentSelected(CHANGE_INFO_CHECK_PW)
            R.id.nav_teacher_calendar -> onFragmentSelected(CALENDAR)
            R.id.nav_teacher_manage_classroom -> onFragmentSelected(MANAGE_CLASSROOM)
            R.id.nav_teacher_apply_classroom -> startApplyClassroomActivity()
            R.id.nav_teacher_manage_accept_academy -> onFragmentSelected(ACCEPT_ACADEMY)
            R.id.nav_teacher_manage_academy -> onFragmentSelected(MANAGE_ACADEMY)
            R.id.nav_teacher_home -> onFragmentSelected(HOME)
            R.id.nav_teacher_manage_request -> onFragmentSelected(MANAGE_REQUEST)
        }
        binding.teacherDrawerLayout.closeDrawer(binding.navigationViewTeacher)
        return true
    }

    private fun startApplyClassroomActivity(){
        val intent = Intent(this, ApplyClassroomActivity::class.java)
        startActivity(intent)
    }

    private fun onFragmentSelected(position: Int) {
        var curFragment: Fragment? = null
        curPosition = position
        when(curPosition){
            CHANGE_INFO -> {
                curFragment = TeacherChangePersonInfoFragment()
            }
            CALENDAR -> {
                curFragment = calendarFragment
            }
            CHANGE_INFO_CHECK_PW -> {
                curFragment = changeInfoCheckPwFragment
            }
            MANAGE_CLASSROOM -> {
                curFragment = manageClassroomFragment
            }
            ACCEPT_ACADEMY -> {
                curFragment = acceptApplyAcademyFragment
            }
            MANAGE_ACADEMY -> {
                curFragment = TeacherManageAcademyFragment()
            }
            HOME -> {
                curFragment = teacherHomeFragment
            }
            MANAGE_REQUEST -> {
                curFragment = ManageRequestFragment(true)
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
        lifecycleScope.launch {
            service.getOwnedAcademyByResponse(teacherId).let{response ->
                if(response.isSuccessful){
                    if(response.body()?.isNotEmpty() == true)
                        binding.navigationViewTeacher.menu.getItem(7).isVisible = true
                }else{
                    Toast.makeText(applicationContext,"소유 학원 정보를 확인할 수 없습니다",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(PREVIOUS_FRAGMENT,curPosition!!)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        onFragmentSelected(savedInstanceState.getInt(PREVIOUS_FRAGMENT))
        super.onRestoreInstanceState(savedInstanceState)
    }

    val CHANGE_INFO = 200
    val CALENDAR = 201
    val EXAM_LIST = 202
    val MANAGE_CLASSROOM = 203
    val PREVIOUS_EXAM = 204
    val CHANGE_INFO_CHECK_PW = 205
    val APPLY_CLASSROOM = 206
    val ACCEPT_ACADEMY = 207
    val MANAGE_ACADEMY = 208
    val HOME = 209
    val MANAGE_REQUEST = 210
    private val PREVIOUS_FRAGMENT = "previous_fragment"
}