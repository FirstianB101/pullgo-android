package com.harry.pullgo.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.data.objects.Lesson
import com.harry.pullgo.data.repository.LessonsRepository
import com.harry.pullgo.databinding.DialogLessonInfoBinding
import com.harry.pullgo.ui.calendar.LessonsViewModel
import com.harry.pullgo.ui.calendar.LessonsViewModelFactory
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class FragmentLessonInfoDialog(private val selectedLesson: Lesson) : DialogFragment() {
    private val binding by lazy{DialogLessonInfoBinding.inflate(layoutInflater)}

    lateinit var viewModel: LessonsViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())

        setLessonInformation()
        initViewModel()

        builder.setView(binding.root)
            .setPositiveButton("확인") { _: DialogInterface?, _: Int -> dismiss() }
        return builder.create()
    }

    private fun setLessonInformation() {
        binding.textViewLessonInfoLessonName.text = selectedLesson.name
        binding.textViewLessonInfoLessonDate.text = selectedLesson.schedule?.date

        val hour = changeDateFormat(selectedLesson.schedule?.beginTime!!)
        val minute = changeDateFormat(selectedLesson.schedule?.endTime!!)
        binding.textViewLessonInfoLessonTime.text = "$hour ~ $minute"
    }

    private fun initViewModel(){
        val factory = LessonsViewModelFactory(LessonsRepository())
        viewModel = ViewModelProvider(requireActivity(),factory).get(LessonsViewModel::class.java)

        viewModel.academyInfoRepository.observe(requireActivity()){
            binding.textViewLessonInfoAcademyName.text = it.name
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