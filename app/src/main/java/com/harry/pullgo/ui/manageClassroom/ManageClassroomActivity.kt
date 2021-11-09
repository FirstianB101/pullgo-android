package com.harry.pullgo.ui.manageClassroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.harry.pullgo.R
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.databinding.ActivityManageClassroomBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageClassroomActivity : AppCompatActivity() {
    val binding by lazy{ActivityManageClassroomBinding.inflate(layoutInflater)}

    private lateinit var selectedClassroom: Classroom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        setListeners()
    }

    private fun initialize(){
        selectedClassroom = intent?.getSerializableExtra("selectedClassroom") as Classroom

        val editClassroomFragment = EditClassroomFragment(selectedClassroom)
        val manageClassroomPeopleFragment = ManageClassroomPeopleFragment(selectedClassroom)
        val manageClassroomRequestsFragment = ManageClassroomRequestsFragment(selectedClassroom)
        val manageClassroomExamFragment = ManageClassroomExamFragment(selectedClassroom)

        binding.viewPagerManageClassroom.adapter = ManageClassroomPagerAdapter(this,
            listOf(editClassroomFragment,manageClassroomPeopleFragment,manageClassroomRequestsFragment,manageClassroomExamFragment)
        )
    }

    private fun setListeners(){
        binding.viewPagerManageClassroom.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavManageClassroom.menu.getItem(position).isChecked = true
            }
        })

        binding.bottomNavManageClassroom.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_edit_classroom -> {
                    binding.viewPagerManageClassroom.currentItem = 0
                    true
                }
                R.id.nav_manage_student -> {
                    binding.viewPagerManageClassroom.currentItem = 1
                    true
                }
                R.id.nav_manage_request -> {
                    binding.viewPagerManageClassroom.currentItem = 2
                    true
                }
                R.id.nav_manage_exam -> {
                    binding.viewPagerManageClassroom.currentItem = 3
                    true
                }
                else -> false
            }
        }
    }

    class ManageClassroomPagerAdapter(activity: AppCompatActivity, private val fragments: List<Fragment>)
        :FragmentStateAdapter(activity){
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}