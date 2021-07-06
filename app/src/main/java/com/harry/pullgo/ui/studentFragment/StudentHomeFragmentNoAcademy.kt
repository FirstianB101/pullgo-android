package com.harry.pullgo.ui.studentFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.harry.pullgo.ui.FindAcademyActivity
import com.harry.pullgo.R

class StudentHomeFragmentNoAcademy: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_student_home_no_academy,container,false)
        val button=root.findViewById(R.id.buttonStudentSignUpAcademy) as Button

        button.setOnClickListener {
            val intent= Intent(requireContext(), FindAcademyActivity::class.java)
            startActivity(intent)
        }

        return root
    }
}