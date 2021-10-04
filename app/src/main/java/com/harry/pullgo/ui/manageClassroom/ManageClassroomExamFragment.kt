package com.harry.pullgo.ui.manageClassroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.databinding.FragmentManageClassroomManageExamBinding

class ManageClassroomExamFragment(private val selectedClassroom: Classroom): Fragment() {
    private val binding by lazy{FragmentManageClassroomManageExamBinding.inflate(layoutInflater)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return binding.root
    }
}