package com.harry.pullgo.ui.teacherFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harry.pullgo.ui.findAcademy.FindAcademyActivity
import com.harry.pullgo.databinding.FragmentTeacherHomeNoAcademyBinding

class TeacherHomeFragmentNoAcademy: Fragment() {
    private val binding by lazy{FragmentTeacherHomeNoAcademyBinding.inflate(layoutInflater)}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.buttonTeacherSignUpAcademy.setOnClickListener {
            val intent= Intent(requireContext(), FindAcademyActivity::class.java)
            startActivity(intent)
        }

        binding.buttonTeacherMakeAcademy.setOnClickListener {
            
        }

        return binding.root
    }
}