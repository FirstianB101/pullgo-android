package com.harry.pullgo.ui.studentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.harry.pullgo.R

class StudentHomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_student_home, container, false)
        val textView = root.findViewById<TextView>(R.id.textStudentHome)
        textView.text = "홈 프래그먼트"
        return root
    }
}