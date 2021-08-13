package com.harry.pullgo.ui.manageClassroomDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.R
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.data.repository.ManageClassroomDetailsRepository
import com.harry.pullgo.databinding.ActivityManageClassroomBinding

class ManageClassroomDetailsActivity : AppCompatActivity() {
    val binding by lazy{ActivityManageClassroomBinding.inflate(layoutInflater)}

    private lateinit var editClassroomFragment: EditClassroomFragment
    private lateinit var manageStudentFragment: ManageStudentFragment
    private lateinit var manageRequestsFragment: ManageRequestsFragment
    private lateinit var manageExamFragment: ManageExamFragment

    private lateinit var selectedClassroom: Classroom

    private val viewModel: ManageClassroomDetailsViewModel by viewModels{ManageClassroomDetailsViewModelFactory(ManageClassroomDetailsRepository())}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        setListeners()
        initViewModel()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun initialize(){
        val classroomId = intent?.getLongExtra("selectedClassroomId",-1L)
        val classroomAcademyId = intent?.getLongExtra("selectedClassroomAcademyId",-1L)
        val classroomName = intent?.getStringExtra("selectedClassroomName")
        selectedClassroom = Classroom(classroomId,classroomAcademyId,classroomName)

        editClassroomFragment = EditClassroomFragment(selectedClassroom)
        manageStudentFragment = ManageStudentFragment(selectedClassroom)
        manageRequestsFragment = ManageRequestsFragment(selectedClassroom)
        manageExamFragment = ManageExamFragment(selectedClassroom)

        onFragmentSelected(0)
    }

    private fun setListeners(){
        binding.bottomNavManageClassroom.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_edit_classroom -> onFragmentSelected(0)
                R.id.nav_manage_student -> onFragmentSelected(1)
                R.id.nav_manage_request -> onFragmentSelected(2)
                R.id.nav_manage_exam -> onFragmentSelected(3)
                else -> false
            }
        }
    }

    private fun initViewModel(){
        
    }

    private fun onFragmentSelected(position: Int): Boolean{
        var curFragment: Fragment? = null

        when(position){
            0 -> {
                curFragment = editClassroomFragment
                binding.toolbarManageClassroom.title = "반 정보 관리"
            }
            1 -> {
                curFragment = manageStudentFragment
                binding.toolbarManageClassroom.title = "학생 관리"
            }
            2 -> {
                curFragment = manageRequestsFragment
                binding.toolbarManageClassroom.title = "가입 요청 관리"
            }
            3 -> {
                curFragment = manageExamFragment
                binding.toolbarManageClassroom.title = "시험 관리"
            }
        }

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        transaction.replace(R.id.mainFragmentManageClassroom, curFragment!!).addToBackStack(null).commit()

        return true
    }
}