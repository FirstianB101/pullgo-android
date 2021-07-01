package com.harry.pullgo.studentFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.harry.pullgo.R
import android.widget.TextView
import androidx.fragment.app.Fragment

class StudentChangePersonInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_student_change_info, container, false)
        val textView = root.findViewById<TextView>(R.id.textChangeInfo)
        textView.text = "회원정보 수정 페이지"
        return root
    }
}