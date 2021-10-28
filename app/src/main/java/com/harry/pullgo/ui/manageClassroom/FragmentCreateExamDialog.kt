package com.harry.pullgo.ui.manageClassroom

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.repository.ManageClassroomRepository
import com.harry.pullgo.databinding.DialogCreateExamBinding
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Duration

@AndroidEntryPoint
class FragmentCreateExamDialog(private val selectedClassroomId: Long): DialogFragment() {
    private val binding by lazy{DialogCreateExamBinding.inflate(layoutInflater)}

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
        _dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return _dialog
    }

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
            binding.spinnerTextViewSelectStartTime.setText(String.format("%02d:%02d",beginTimePicker.hour,beginTimePicker.minute))
        }

        endTimePicker.addOnPositiveButtonClickListener {
            binding.spinnerTextViewSelectEndTime.setText(String.format("%02d:%02d",endTimePicker.hour,endTimePicker.minute))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners(){
        binding.buttonCreateNewExam.setOnClickListener {
            if(isAllTextWritten()){
                createExam()
            }
        }

        binding.buttonCancelCreateExam.setOnClickListener {
            dismiss()
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
        viewModel.manageExamMessage.observe(requireActivity()){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            if(it == "시험이 생성되었습니다"){
                parentFragment?.setFragmentResult("isCreatedExam", bundleOf("isCreated" to "yes"))
                dismiss()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createExam(){
        val examName = binding.textExamName.text.toString()
        val beginDateTime = binding.spinnerTextViewSelectStartDate.text.toString() + 'T' +
                String.format("%02d:%02d:00",beginTimePicker.hour,beginTimePicker.minute)
        val endDateTime = binding.spinnerTextViewSelectEndDate.text.toString() + 'T' +
                String.format("%02d:%02d:00",endTimePicker.hour,endTimePicker.minute)
        val timeLimit = Duration.ofMinutes(binding.textExamTimeLimit.text.toString().toLong()).toString()
        val passScore = binding.textExamStandardScore.text.toString().toInt()

        val newExam = Exam(selectedClassroomId, app.loginUser.teacher?.id, examName, beginDateTime, endDateTime,
                            timeLimit,passScore,false,false)
        viewModel.createExam(newExam)
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
}