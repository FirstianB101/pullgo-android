package com.harry.pullgo.ui.manageClassroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationBarView
import com.harry.pullgo.R
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.databinding.ActivityManageClassroomBinding

class ManageClassroomActivity : AppCompatActivity() {
    val binding by lazy{ActivityManageClassroomBinding.inflate(layoutInflater)}

    private lateinit var editClassroomFragment: EditClassroomFragment
    private lateinit var manageStudentFragment: ManageStudentFragment
    private lateinit var manageRequestsFragment: ManageRequestsFragment
    private lateinit var manageExamFragment: ManageExamFragment

    private lateinit var selectedClassroom: Classroom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        setListeners()
    }

    private fun initialize(){
        val classroomId = intent?.getLongExtra("selectedClassroomId",-1L)
        val classroomAcademyId = intent?.getLongExtra("selectedClassroomAcademyId",-1L)
        val classroomName = intent?.getStringExtra("selectedClassroomName")
        selectedClassroom = Classroom(classroomId,classroomAcademyId,classroomName)

        editClassroomFragment = EditClassroomFragment()
        manageStudentFragment = ManageStudentFragment()
        manageRequestsFragment = ManageRequestsFragment()
        manageExamFragment = ManageExamFragment()

        editClassroomFragment.selectedClassroom = selectedClassroom

        supportFragmentManager.beginTransaction().replace(R.id.mainFragmentManageClassroom,editClassroomFragment).commit()
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

    private fun onFragmentSelected(position: Int): Boolean{
        var curFragment: Fragment? = null

        when(position){
            0 -> curFragment = editClassroomFragment
            1 -> curFragment = manageStudentFragment
            2 -> curFragment = manageRequestsFragment
            3 -> curFragment = manageExamFragment
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