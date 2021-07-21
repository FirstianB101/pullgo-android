package com.harry.pullgo.ui.manageClassroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harry.pullgo.databinding.FragmentManageClassroomManageStudentBinding

class ManageStudentFragment: Fragment() {
    private val binding by lazy{FragmentManageClassroomManageStudentBinding.inflate(layoutInflater)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return binding.root
    }
}