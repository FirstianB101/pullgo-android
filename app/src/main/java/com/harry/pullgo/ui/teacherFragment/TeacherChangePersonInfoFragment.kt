package com.harry.pullgo.ui.teacherFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.harry.pullgo.R

class TeacherChangePersonInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_teacher_change_info, container, false)
        val textView = root.findViewById<TextView>(R.id.textChangeInfo)
        textView.text = "회원정보 수정 페이지"
        return root
    }
}