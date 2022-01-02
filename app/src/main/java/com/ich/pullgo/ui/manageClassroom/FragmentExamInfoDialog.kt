package com.ich.pullgo.ui.manageClassroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.ich.pullgo.R
import com.ich.pullgo.data.models.Exam
import com.ich.pullgo.databinding.DialogExamInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentExamInfoDialog(private val selectedExam: Exam)
    : DialogFragment(), FragmentEditExamInfo.ManageExamButtonClickListener, FragmentDetailedManageExam.OnExamStateChangedListener{
    private val binding by lazy{DialogExamInfoBinding.inflate(layoutInflater)}

    private val detailedFragment by lazy {FragmentDetailedManageExam(selectedExam)}
    private val editFragment by lazy {FragmentEditExamInfo(selectedExam)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        editFragment.manageExamButtonClickListener = this
        detailedFragment.examStateChangedListener = this

        childFragmentManager.beginTransaction().add(R.id.containerExamInfo, editFragment).commit()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val width = resources.getDimensionPixelSize(R.dimen.manage_exam_dialog_width)
        val height = resources.getDimensionPixelSize(R.dimen.manage_exam_dialog_height)
        dialog!!.window!!.setLayout(width, height)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.rounded)
    }

    override fun onButtonClicked() {
        childFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        ).replace(R.id.containerExamInfo, detailedFragment).commit()
    }

    override fun onExamEdited() {
       parentFragment?.setFragmentResult("isExamEdited", bundleOf("isEdited" to "yes"))
    }

    override fun onExamChange() {
        parentFragment?.setFragmentResult("isExamEdited", bundleOf("isEdited" to "yes"))
        dismiss()
    }
}