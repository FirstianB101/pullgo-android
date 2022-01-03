package com.ich.pullgo.ui.manageClassroom

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
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/manageClassroom/FragmentCreateExamDialog.kt
import com.ich.pullgo.data.models.Exam
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.DialogCreateExamBinding
=======
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.DialogCreateExamBinding
import com.ich.pullgo.domain.model.Exam
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/manageClassroom/FragmentCreateExamDialog.kt
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Duration
import javax.inject.Inject

@AndroidEntryPoint
class FragmentCreateExamDialog(private val selectedClassroomId: Long): DialogFragment() {
    private val binding by lazy{DialogCreateExamBinding.inflate(layoutInflater)}

    private lateinit var beginDatePicker: MaterialDatePicker<Long>
    private lateinit var endDatePicker: MaterialDatePicker<Long>
    private lateinit var beginTimePicker: MaterialTimePicker
    private lateinit var endTimePicker: MaterialTimePicker

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ManageClassroomViewModel by viewModels()

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
            binding.spinnerTextViewSelectBeginDate.setText(MillToDate(it).toString())
        }

        endDatePicker.addOnPositiveButtonClickListener {
            binding.spinnerTextViewSelectEndDate.setText(MillToDate(it).toString())
        }

        beginTimePicker.addOnPositiveButtonClickListener {
            binding.spinnerTextViewSelectBeginTime.setText(String.format("%02d:%02d",beginTimePicker.hour,beginTimePicker.minute))
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

        binding.spinnerTextViewSelectBeginDate.setOnClickListener {
            beginDatePicker.show(childFragmentManager,"beginDate")
        }

        binding.spinnerTextViewSelectEndDate.setOnClickListener {
            endDatePicker.show(childFragmentManager,"endDate")
        }

        binding.spinnerTextViewSelectBeginTime.setOnClickListener {
            beginTimePicker.show(childFragmentManager,"beginTime")
        }

        binding.spinnerTextViewSelectEndTime.setOnClickListener {
            endTimePicker.show(childFragmentManager,"endTime")
        }
    }

    private fun initViewModel(){
        viewModel.examMessage.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"${it.data}",Toast.LENGTH_SHORT).show()
                    if(it.data == "시험이 생성되었습니다") {
                        parentFragment?.setFragmentResult("isExamEdited", bundleOf("isEdited" to "yes"))
                        dismiss()
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createExam(){
        val examName = binding.textExamName.text.toString()
        val beginDateTime = binding.spinnerTextViewSelectBeginDate.text.toString() + 'T' +
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
            binding.spinnerTextViewSelectBeginDate.text.isNotEmpty() &&
            binding.spinnerTextViewSelectEndDate.text.isNotEmpty() &&
            binding.spinnerTextViewSelectEndDate.text.isNotEmpty() &&
            binding.spinnerTextViewSelectEndTime.text.isNotEmpty()

    private fun MillToDate(mills: Long): String? {
        val pattern = "yyyy-MM-dd"
        val formatter = SimpleDateFormat(pattern)
        return formatter.format(Timestamp(mills))
    }
}