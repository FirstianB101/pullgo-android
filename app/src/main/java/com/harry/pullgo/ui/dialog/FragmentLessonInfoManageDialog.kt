package com.harry.pullgo.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.harry.pullgo.R
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.databinding.DialogLessonInfoManageBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class FragmentLessonInfoManageDialog :DialogFragment(){
    private val binding by lazy{DialogLessonInfoManageBinding.inflate(layoutInflater)}
    private var selectedDate: Long? = null
    private var startHour = -1
    private var startMinute = -1
    private var endHour = -1
    private var endMinute = -1
    private var selectedClassroom: Classroom? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        initialize()
        setListeners()
        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return _dialog
    }

    private fun setListeners() {
        binding.buttonLessonInfoSave.setOnClickListener {
            editModeOn()
        }

        binding.buttonLessonInfoRemove.setOnClickListener {
            Snackbar.make(binding.root,"삭제 버튼",Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun editModeOn(){
        binding.textLessonInfoNewLessonName.isEnabled = true
        binding.spinnerLessonInfoTextViewSelectDate.isEnabled = true
        binding.spinnerTextViewLessonInfoSelectStartTime.isEnabled = true
        binding.spinnerTextViewLessonInfoSelectEndTime.isEnabled = true
        binding.spinnerLessonInfoSelectClassroom.isEnabled = true

        binding.textLessonInfoNewLessonName.requestFocus()

        Toast.makeText(requireContext(),"편집 모드로 전환되었습니다",Toast.LENGTH_SHORT).show()
    }

    private fun initialize(){
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
                binding.spinnerLessonInfoTextViewSelectDate.setText(MillToDate(selectedDate!!).toString())
            }else{
                Snackbar.make(binding.root,"과거 날짜는 선택될 수 없습니다",Snackbar.LENGTH_SHORT).show()
            }
        }

        startTimePicker.addOnPositiveButtonClickListener {
            startHour = startTimePicker.hour
            startMinute = startTimePicker.minute
            binding.spinnerTextViewLessonInfoSelectStartTime.setText("${startHour}시 ${startMinute}분 ~")
            resetIfTimeNotAppropriate()
        }

        endTimePicker.addOnPositiveButtonClickListener {
            endHour = endTimePicker.hour
            endMinute = endTimePicker.minute
            binding.spinnerTextViewLessonInfoSelectEndTime.setText("${endHour}시 ${endMinute}분 까지")
            resetIfTimeNotAppropriate()
        }

        binding.spinnerLessonInfoTextViewSelectDate.setOnClickListener {
            datePicker.show(childFragmentManager,"date")
        }

        binding.spinnerTextViewLessonInfoSelectStartTime.setOnClickListener {
            startTimePicker.show(childFragmentManager,"startTime")
        }

        binding.spinnerTextViewLessonInfoSelectEndTime.setOnClickListener {
            endTimePicker.show(childFragmentManager,"endTime")
        }
    }

    private fun resetIfTimeNotAppropriate(){
        if(startHour == -1 || startMinute == -1 || endHour == -1 || endMinute == -1)return

        if(startHour > endHour || (startHour == endHour && startMinute > endMinute)){
            startHour = -1
            startMinute = -1
            endHour = -1
            endMinute = -1
            binding.spinnerTextViewLessonInfoSelectStartTime.setText("")
            binding.spinnerTextViewLessonInfoSelectEndTime.setText("")
            Snackbar.make(binding.root,"잘못된 시간 정보입니다",Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun MillToDate(mills: Long): String? {
        val pattern = "yyyy-MM-dd"
        val formatter = SimpleDateFormat(pattern)
        return formatter.format(Timestamp(mills))
    }

    private fun isSelectedAllOptions(): Boolean =
        (startHour != -1 && startMinute != -1 && endHour != -1 && endMinute != -1
                && selectedDate != null && selectedClassroom != null)

    companion object {
        const val TAG_LESSON_INFO_MANAGE_DIALOG = "lesson_info_manage_dialog"
    }
}