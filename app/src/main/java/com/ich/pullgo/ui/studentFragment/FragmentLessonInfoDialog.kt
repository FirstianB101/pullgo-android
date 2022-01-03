package com.ich.pullgo.ui.studentFragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.DialogLessonInfoBinding
import com.ich.pullgo.domain.model.Lesson
import com.ich.pullgo.ui.calendar.LessonsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentLessonInfoDialog(private val selectedLesson: Lesson) : DialogFragment() {
    private val binding by lazy{DialogLessonInfoBinding.inflate(layoutInflater)}

    private val viewModel: LessonsViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())

        setLessonInformation()
        initViewModel()
        setListeners()

        builder.setView(binding.root)
        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        return _dialog
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
        viewModel.academyInfoRepository.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    binding.textViewLessonInfoAcademyName.text = it.data?.name
                }
                Status.ERROR -> {
                    binding.textViewLessonInfoAcademyName.text = "정보를 불러올 수 없습니다"
                }
            }
        }

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