package com.harry.pullgo.ui.manageClassroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.harry.pullgo.R
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.databinding.ActivityManageClassroomBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageClassroomActivity : AppCompatActivity() {
    val binding by lazy{ActivityManageClassroomBinding.inflate(layoutInflater)}

    private lateinit var editClassroomFragment: EditClassroomFragment
    private lateinit var manageClassroomPeopleFragment: ManageClassroomPeopleFragment
    private lateinit var manageClassroomRequestsFragment: ManageClassroomRequestsFragment
    private lateinit var manageClassroomExamFragment: ManageClassroomExamFragment

    private lateinit var selectedClassroom: Classroom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        setListeners()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun initialize(){
        selectedClassroom = intent?.getSerializableExtra("selectedClassroom") as Classroom

        editClassroomFragment = EditClassroomFragment(selectedClassroom)
        manageClassroomPeopleFragment = ManageClassroomPeopleFragment(selectedClassroom)
        manageClassroomRequestsFragment = ManageClassroomRequestsFragment(selectedClassroom)
        manageClassroomExamFragment = ManageClassroomExamFragment(selectedClassroom)

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

    private fun onFragmentSelected(position: Int): Boolean{
        var curFragment: Fragment? = null

        when(position){
            0 -> {
                curFragment = editClassroomFragment
                binding.toolbarManageClassroom.title = "반 정보 관리"
            }
            1 -> {
                curFragment = manageClassroomPeopleFragment
                binding.toolbarManageClassroom.title = "구성원 관리"
            }
            2 -> {
                curFragment = manageClassroomRequestsFragment
                binding.toolbarManageClassroom.title = "가입 요청 관리"
            }
            3 -> {
                curFragment = manageClassroomExamFragment
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