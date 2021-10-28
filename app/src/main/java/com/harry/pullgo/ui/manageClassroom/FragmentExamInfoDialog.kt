package com.harry.pullgo.ui.manageClassroom

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.repository.ManageClassroomRepository
import com.harry.pullgo.databinding.DialogExamInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Duration

@AndroidEntryPoint
class FragmentExamInfoDialog(private val selectedExam: Exam): DialogFragment() {
    private val binding by lazy{DialogExamInfoBinding.inflate(layoutInflater)}

    private lateinit var beginDatePicker: MaterialDatePicker<Long>
    private lateinit var endDatePicker: MaterialDatePicker<Long>
    private lateinit var beginTimePicker: MaterialTimePicker
    private lateinit var endTimePicker: MaterialTimePicker

    private val viewModel: ManageClassroomViewModel by viewModels()

    private val app: PullgoApplication by lazy{requireActivity().application as PullgoApplication }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())

        initialize()
        setListeners()
        initViewModel()

        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        _dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return _dialog
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initialize(){
        beginDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .build()

        endDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .build()

        beginTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .build()

        endTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .build()

        beginDatePicker.addOnPositiveButtonClickListener {
            binding.spinnerTextViewSelectStartDate.setText(MillToDate(it).toString())
        }

        endDatePicker.addOnPositiveButtonClickListener {
            binding.spinnerTextViewSelectEndDate.setText(MillToDate(it).toString())
        }

        beginTimePicker.addOnPositiveButtonClickListener {
            binding.spinnerTextViewSelectStartTime.setText("${beginTimePicker.hour}:${beginTimePicker.minute}")
        }

        endTimePicker.addOnPositiveButtonClickListener {
            binding.spinnerTextViewSelectEndTime.setText("${endTimePicker.hour}:${endTimePicker.minute}")
        }

        binding.textExamName.setText(selectedExam.name)
        binding.textExamStandardScore.setText(selectedExam.passScore.toString())
        binding.textExamTimeLimit.setText(translatePTFormat(selectedExam.timeLimit!!))
        val beginDateTime = selectedExam.beginDateTime?.split('T')
        binding.spinnerTextViewSelectStartDate.setText(beginDateTime?.get(0))
        binding.spinnerTextViewSelectStartTime.setText(beginDateTime?.get(1))
        val endDateTime = selectedExam.endDateTime?.split('T')
        binding.spinnerTextViewSelectEndDate.setText(endDateTime?.get(0))
        binding.spinnerTextViewSelectEndTime.setText(endDateTime?.get(1))
    }

    private fun setListeners(){
        binding.buttonEditExam.setOnClickListener {
            if(isAllTextWritten()){
                editExam()
            }
        }

        binding.buttonFinishExam.setOnClickListener {

        }

        binding.buttonCancelExam.setOnClickListener {

        }

        binding.spinnerTextViewSelectStartDate.setOnClickListener {
            beginDatePicker.show(childFragmentManager,"beginDate")
        }

        binding.spinnerTextViewSelectEndDate.setOnClickListener {
            endDatePicker.show(childFragmentManager,"endDate")
        }

        binding.spinnerTextViewSelectStartTime.setOnClickListener {
            beginTimePicker.show(childFragmentManager,"beginTime")
        }

        binding.spinnerTextViewSelectEndTime.setOnClickListener {
            endTimePicker.show(childFragmentManager,"endTime")
        }
    }

    private fun initViewModel(){

    }

    private fun editExam(){

    }

    private fun isAllTextWritten() = binding.textExamName.text!!.isNotEmpty() &&
            binding.textExamStandardScore.text!!.isNotEmpty() &&
            binding.textExamTimeLimit.text!!.isNotEmpty() &&
            binding.spinnerTextViewSelectStartDate.text.isNotEmpty() &&
            binding.spinnerTextViewSelectEndDate.text.isNotEmpty() &&
            binding.spinnerTextViewSelectEndDate.text.isNotEmpty() &&
            binding.spinnerTextViewSelectEndTime.text.isNotEmpty()

    private fun MillToDate(mills: Long): String? {
        val pattern = "yyyy-MM-dd"
        val formatter = SimpleDateFormat(pattern)
        return formatter.format(Timestamp(mills))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun translatePTFormat(time: String): String{
        val duration = Duration.parse(time)
        return duration.toMinutes().toString()
    }
}