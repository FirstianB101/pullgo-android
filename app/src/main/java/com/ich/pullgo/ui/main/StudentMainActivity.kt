package com.ich.pullgo.ui.main

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
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.api.OnCheckPwListener
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.ActivityStudentMainBinding
import com.ich.pullgo.ui.applyClassroom.ApplyClassroomActivity
import com.ich.pullgo.ui.calendar.CalendarFragment
import com.ich.pullgo.ui.commonFragment.ChangeInfoCheckPwFragment
import com.ich.pullgo.ui.commonFragment.ChangeInfoViewModel
import com.ich.pullgo.ui.commonFragment.ManageRequestFragment
import com.ich.pullgo.ui.findAcademy.FindAcademyActivity
import com.ich.pullgo.ui.studentFragment.StudentChangePersonInfoFragment
import com.ich.pullgo.ui.studentFragment.StudentExamHistoryFragment
import com.ich.pullgo.ui.studentFragment.StudentExamListFragment
import com.ich.pullgo.ui.studentFragment.StudentHomeFragmentNoAcademy
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudentMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private val binding by lazy{ActivityStudentMainBinding.inflate(layoutInflater)}
    lateinit var studentChangeInfoFragment: StudentChangePersonInfoFragment
    lateinit var checkPwFragment: ChangeInfoCheckPwFragment
    lateinit var manageRequestFragment: ManageRequestFragment
    lateinit var calendarFragment: CalendarFragment
    lateinit var studentExamListFragment: StudentExamListFragment
    lateinit var studentExamHistoryFragment: StudentExamHistoryFragment
    lateinit var studentHomeFragment: StudentHomeFragmentNoAcademy

    @Inject
    lateinit var app: PullgoApplication

    private val changeInfoViewModel: ChangeInfoViewModel by viewModels()

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
            when(it.status){
                Status.SUCCESS -> {
                    app.loginUser.student = it.data
                    headerView.findViewById<TextView>(R.id.textViewNavFullName).text = "${it.data?.account?.fullName}님"
                    headerView.findViewById<TextView>(R.id.textViewNavId).text = "${it.data?.account?.username}"

                    Toast.makeText(this,"회원정보가 수정되었습니다",Toast.LENGTH_SHORT).show()
                    if(intent.getBooleanExtra("appliedAcademyExist",false)) onFragmentSelected(CALENDAR)
                    else onFragmentSelected(HOME)
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
        studentChangeInfoFragment = StudentChangePersonInfoFragment()
        manageRequestFragment = ManageRequestFragment(false)
        checkPwFragment = ChangeInfoCheckPwFragment(object: OnCheckPwListener{
            override fun onPasswordChecked() {
                onFragmentSelected(CHANGE_INFO)
            }
        })

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
        headerView.findViewById<TextView>(R.id.textViewNavFullName).text = "${app.loginUser.student?.account?.fullName}님"
        headerView.findViewById<TextView>(R.id.textViewNavId).text = "${app.loginUser.student?.account?.username}"
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
            app.loginUser.student = null
            app.loginUser.teacher = null
            app.loginUser.token = null
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
                curFragment = studentChangeInfoFragment
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
                curFragment = checkPwFragment
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