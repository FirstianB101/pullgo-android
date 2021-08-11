package com.harry.pullgo.ui.studentFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.harry.pullgo.ui.findAcademy.FindAcademyActivity
import com.harry.pullgo.R
import com.harry.pullgo.databinding.FragmentStudentHomeNoAcademyBinding

class StudentHomeFragmentNoAcademy: Fragment() {
    private val binding by lazy{FragmentStudentHomeNoAcademyBinding.inflate(layoutInflater)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding.buttonStudentSignUpAcademy.setOnClickListener {
            val intent= Intent(requireContext(), FindAcademyActivity::class.java)
            intent.putExtra("calledByStudent",true)
            startActivity(intent)
        }

        return binding.root
    }
}