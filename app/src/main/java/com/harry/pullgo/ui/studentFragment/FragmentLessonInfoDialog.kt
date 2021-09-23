package com.harry.pullgo.ui.studentFragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.data.models.Lesson
import com.harry.pullgo.data.repository.LessonsRepository
import com.harry.pullgo.databinding.DialogLessonInfoBinding
import com.harry.pullgo.ui.calendar.LessonsViewModel
import com.harry.pullgo.ui.calendar.LessonsViewModelFactory

class FragmentLessonInfoDialog(private val selectedLesson: Lesson) : DialogFragment() {
    private val binding by lazy{DialogLessonInfoBinding.inflate(layoutInflater)}

    private val viewModel: LessonsViewModel by activityViewModels{LessonsViewModelFactory(LessonsRepository())}

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())

        setLessonInformation()
        initViewModel()
        setListeners()

        builder.setView(binding.root)
        return builder.create()
    }

    private fun setListeners(){
        binding.buttonDialogLessonInfo.setOnClickListener { dismiss() }
    }

    private fun setLessonInformation() {
        binding.textViewLessonInfoLessonName.text = selectedLesson.name
        binding.textViewLessonInfoLessonDate.text = selectedLesson.schedule?.date

        val hour = changeDateFormat(selectedLesson.schedule?.beginTime!!)
        val minute = changeDateFormat(selectedLesson.schedule?.endTime!!)
        binding.textViewLessonInfoLessonTime.text = "$hour ~ $minute"
    }

    private fun initViewModel(){
        viewModel.classroomInfoRepository.observe(requireActivity()){
            val classroom = it.name!!.split(';')
            binding.textViewLessonInfoClassroomName.text = "${classroom[0]} (${classroom[1]})"
        }

        viewModel.academyInfoRepository.observe(requireActivity()){
            binding.textViewLessonInfoAcademyName.text = it.name
        }

        viewModel.getClassroomInfoOfLesson(selectedLesson)
        viewModel.getAcademyInfoOfLesson(selectedLesson)
    }

    private fun changeDateFormat(date: String): String{
        val time = date.split(':')
        return "${time[0]}:${time[1]}"
    }

    companion object {
        const val TAG_LESSON_INFO_DIALOG = "lesson_info_dialog"
    }
}