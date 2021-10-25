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
import com.google.android.material.navigation.NavigationView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnCheckPwListener
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.ChangeInfoRepository
import com.harry.pullgo.databinding.ActivityStudentMainBinding
import com.harry.pullgo.ui.applyClassroom.ApplyClassroomActivity
import com.harry.pullgo.ui.calendar.CalendarFragment
import com.harry.pullgo.ui.commonFragment.ChangeInfoCheckPwFragment
import com.harry.pullgo.ui.commonFragment.ManageRequestFragment
import com.harry.pullgo.ui.findAcademy.FindAcademyActivity
import com.harry.pullgo.ui.studentFragment.StudentChangePersonInfoFragment
import com.harry.pullgo.ui.studentFragment.StudentExamHistoryFragment
import com.harry.pullgo.ui.studentFragment.StudentExamListFragment
import com.harry.pullgo.ui.studentFragment.StudentHomeFragmentNoAcademy

class StudentMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private val binding by lazy{ActivityStudentMainBinding.inflate(layoutInflater)}
    lateinit var studentChangeInfoFragment: StudentChangePersonInfoFragment
    lateinit var changeInfoCheckPwFragment: ChangeInfoCheckPwFragment
    lateinit var calendarFragment: CalendarFragment
    lateinit var studentExamListFragment: StudentExamListFragment
    lateinit var studentExamHistoryFragment: StudentExamHistoryFragment
    lateinit var studentHomeFragment: StudentHomeFragmentNoAcademy
    lateinit var manageRequestFragment: ManageRequestFragment

    private val changeInfoViewModel: ChangeInfoViewModel by viewModels{ChangeInfoViewModelFactory(
        ChangeInfoRepository(applicationContext)
    )}

    private lateinit var headerView: View
    private var curPosition: Int? = null

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
            changeInfoViewModel.changeStudentInfo(it.id!!,it)
            LoginInfo.user?.student = it
            headerView.findViewById<TextView>(R.id.textViewNavFullName).text = "${it.account?.fullName}님"
            headerView.findViewById<TextView>(R.id.textViewNavId).text = "${it.account?.username}"
            onFragmentSelected(CALENDAR)
        }

        changeInfoViewModel.changeInfoMessage.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }
    }

    private fun initialize(){
        studentChangeInfoFragment = StudentChangePersonInfoFragment()
        changeInfoCheckPwFragment = ChangeInfoCheckPwFragment()
        manageRequestFragment = ManageRequestFragment(false)

        if(intent.getBooleanExtra("appliedAcademyExist",false)){
            calendarFragment = CalendarFragment()
            studentExamListFragment = StudentExamListFragment()
            studentExamHistoryFragment = StudentExamHistoryFragment()

            supportFragmentManager.beginTransaction().replace(R.id.studentMainFragment, calendarFragment).commit()
            curPosition = CALENDAR

            binding.navigationViewStudent.menu.clear()
            binding.navigationViewStudent.inflateMenu(R.menu.activity_student_main_drawer)
            binding.textViewStudentApplyOtherAcademy.visibility = View.VISIBLE
        }else{
            studentHomeFragment = StudentHomeFragmentNoAcademy()

            supportFragmentManager.beginTransaction().replace(R.id.studentMainFragment, studentHomeFragment).commit()
            curPosition = HOME
        }

        headerView = binding.navigationViewStudent.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.textViewNavFullName).text="${LoginInfo.user?.student?.account?.fullName}님"
        headerView.findViewById<TextView>(R.id.textViewNavId).text="${LoginInfo.user?.student?.account?.username}"
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
            LoginInfo.user?.student = null
            LoginInfo.user?.teacher = null
            LoginInfo.user?.token = null
            finish()
        }

        changeInfoCheckPwFragment.pwCheckListenerListener = object: OnCheckPwListener{
            override fun onPasswordCheck() {
                onFragmentSelected(CHANGE_INFO)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_student_change_info -> onFragmentSelected(CHANGE_INFO_CHECK_PW)
            R.id.nav_student_calendar -> onFragmentSelected(CALENDAR)
            R.id.nav_student_exam_list -> onFragmentSelected(EXAM_LIST)
            R.id.nav_student_previous_exam -> onFragmentSelected(PREVIOUS_EXAM)
            R.id.nav_student_home -> onFragmentSelected(HOME)
            R.id.nav_student_apply_classroom -> startApplyClassroomActivity()
            R.id.nav_student_manage_request -> onFragmentSelected(MANAGE_REQUEST)
        }
        binding.studentDrawerLayout.closeDrawer(binding.navigationViewStudent)
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
                curFragment = StudentChangePersonInfoFragment()
            }
            CALENDAR -> {
                curFragment = calendarFragment
            }
            EXAM_LIST -> {
                curFragment = studentExamListFragment
            }
            PREVIOUS_EXAM -> {
                curFragment = studentExamHistoryFragment
            }
            CHANGE_INFO_CHECK_PW -> {
                curFragment = changeInfoCheckPwFragment
            }
            HOME -> {
                curFragment = studentHomeFragment
            }
            MANAGE_REQUEST -> {
                curFragment = manageRequestFragment
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
        transaction.replace(R.id.studentMainFragment, curFragment!!).addToBackStack(null).commit()
    }

    private fun startFindAcademyActivity(){
        val intent = Intent(applicationContext, FindAcademyActivity::class.java)
        intent.putExtra("calledByStudent",true)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(PREVIOUS_FRAGMENT,curPosition!!)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        onFragmentSelected(savedInstanceState.getInt(PREVIOUS_FRAGMENT))
        super.onRestoreInstanceState(savedInstanceState)
    }

    val CHANGE_INFO = 100
    val CALENDAR = 101
    val EXAM_LIST = 102
    val PREVIOUS_EXAM = 103
    val CHANGE_INFO_CHECK_PW = 104
    val APPLY_CLASSROOM = 105
    val HOME = 106
    val MANAGE_REQUEST = 107
    private val PREVIOUS_FRAGMENT = "previous_fragment"
}