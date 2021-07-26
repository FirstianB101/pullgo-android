package com.harry.pullgo.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.harry.pullgo.R
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.data.objects.Lesson
import com.harry.pullgo.data.repository.LessonsRepository
import com.harry.pullgo.databinding.DialogLessonInfoManageBinding
import com.harry.pullgo.ui.calendar.LessonsViewModel
import com.harry.pullgo.ui.calendar.LessonsViewModelFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class FragmentLessonInfoManageDialog(private val selectedLesson: Lesson) :DialogFragment(){
    private val binding by lazy{DialogLessonInfoManageBinding.inflate(layoutInflater)}
    private var selectedDate: Long? = null
    private var startHour = -1
    private var startMinute = -1
    private var endHour = -1
    private var endMinute = -1

    lateinit var viewModel: LessonsViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())

        initialize()
        initViewModel()
        setListeners()

        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return _dialog
    }

    private fun initialize(){
        initLessonInfo()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("")
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .build()

        val startTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setTitleText("")
            .build()

        val endTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setTitleText("")
            .build()

        datePicker.addOnPositiveButtonClickListener {
            selectedDate = it
            if(selectedDate!! + TimeUnit.DAYS.toMillis(1) - System.currentTimeMillis() > 0) {
                binding.textViewLessonInfoManageSelectDate.setText(MillToDate(selectedDate!!).toString())
            }else{
                Snackbar.make(binding.root,"과거 날짜는 선택될 수 없습니다",Snackbar.LENGTH_SHORT).show()
            }
        }

        startTimePicker.addOnPositiveButtonClickListener {
            startHour = startTimePicker.hour
            startMinute = startTimePicker.minute
            binding.textViewLessonInfoManageSelectStartTime.setText("${startHour}시 ${startMinute}분 ~")
            resetIfTimeNotAppropriate()
        }

        endTimePicker.addOnPositiveButtonClickListener {
            endHour = endTimePicker.hour
            endMinute = endTimePicker.minute
            binding.textViewLessonInfoManageSelectEndTime.setText("${endHour}시 ${endMinute}분 까지")
            resetIfTimeNotAppropriate()
        }

        binding.textViewLessonInfoManageSelectDate.setOnClickListener {
            datePicker.show(childFragmentManager,"date")
        }

        binding.textViewLessonInfoManageSelectStartTime.setOnClickListener {
            startTimePicker.show(childFragmentManager,"startTime")
        }

        binding.textViewLessonInfoManageSelectEndTime.setOnClickListener {
            endTimePicker.show(childFragmentManager,"endTime")
        }
    }

    private fun setListeners() {
        binding.buttonLessonInfoManageSave.setOnClickListener {
            editModeOn()
        }

        binding.buttonLessonInfoManageRemove.setOnClickListener {
            Snackbar.make(binding.root,"삭제 버튼",Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun editModeOn(){
        binding.textLessonInfoManageLessonName.isEnabled = true
        binding.textViewLessonInfoManageSelectDate.isEnabled = true
        binding.textViewLessonInfoManageSelectStartTime.isEnabled = true
        binding.textViewLessonInfoManageSelectEndTime.isEnabled = true

        binding.textLessonInfoManageLessonName.requestFocus()

        Toast.makeText(requireContext(),"편집 모드로 전환되었습니다",Toast.LENGTH_SHORT).show()
    }

    private fun initViewModel(){
        val factory = LessonsViewModelFactory(LessonsRepository())
        viewModel = ViewModelProvider(requireActivity(),factory).get(LessonsViewModel::class.java)

        viewModel.classroomInfoRepository.observe(requireActivity()){
            binding.textViewLessonInfoManageSelectClassroom.setText(it.name!!.split(';')[0])
        }

        viewModel.getClassroomInfoOfLesson(selectedLesson)
    }

    private fun initLessonInfo(){
        binding.textLessonInfoManageLessonName.setText(selectedLesson.name)
        binding.textViewLessonInfoManageSelectDate.setText(selectedLesson.schedule?.date)
        binding.textViewLessonInfoManageSelectStartTime.setText(selectedLesson.schedule?.beginTime)
        binding.textViewLessonInfoManageSelectEndTime.setText(selectedLesson.schedule?.endTime)
    }

    private fun resetIfTimeNotAppropriate(){
        if(startHour == -1 || startMinute == -1 || endHour == -1 || endMinute == -1)return

        if(startHour > endHour || (startHour == endHour && startMinute > endMinute)){
            startHour = -1
            startMinute = -1
            endHour = -1
            endMinute = -1
            binding.textViewLessonInfoManageSelectStartTime.setText("")
            binding.textViewLessonInfoManageSelectEndTime.setText("")
            Snackbar.make(binding.root,"잘못된 시간 정보입니다",Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun MillToDate(mills: Long): String? {
        val pattern = "yyyy-MM-dd"
        val formatter = SimpleDateFormat(pattern)
        return formatter.format(Timestamp(mills))
    }

    companion object {
        const val TAG_LESSON_INFO_MANAGE_DIALOG = "lesson_info_manage_dialog"
    }
}