package com.harry.pullgo.ui.manageClassroom

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.FragmentEditExamInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Duration
import javax.inject.Inject

@AndroidEntryPoint
class FragmentEditExamInfo(private val selectedExam: Exam): Fragment() {
    private val binding by lazy{FragmentEditExamInfoBinding.inflate(layoutInflater)}

    interface ManageExamButtonClickListener{
        fun onButtonClicked()
        fun onExamEdited()
    }
    var manageExamButtonClickListener: ManageExamButtonClickListener? = null

    private lateinit var beginDatePicker: MaterialDatePicker<Long>
    private lateinit var endDatePicker: MaterialDatePicker<Long>
    private lateinit var beginTimePicker: MaterialTimePicker
    private lateinit var endTimePicker: MaterialTimePicker

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ManageClassroomViewModel by viewModels()

    private var isEditMode = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize()
        setListeners()
        initViewModel()

        return binding.root
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

        refreshExamInfo(selectedExam)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners(){
        binding.buttonEditExam.setOnClickListener {
            if(isEditMode){
                if(isAllTextWritten()){
                    editModeOff()
                    editExam()
                }else{
                    Toast.makeText(requireContext(),"입력되지 않은 정보가 존재합니다",Toast.LENGTH_SHORT).show()
                }
            }else{
                editModeOn()
            }
        }

        binding.buttonDetailedManageExam.setOnClickListener {
            manageExamButtonClickListener?.onButtonClicked()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViewModel(){
        viewModel.editExam.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"시험 정보가 수정되었습니다", Toast.LENGTH_SHORT).show()
                    refreshExamInfo(it.data!!)
                    manageExamButtonClickListener?.onExamEdited()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"시험 정보를 수정하지 못했습니다 ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun editExam(){
        val examName = binding.textExamName.text.toString()
        val beginDateTime = binding.spinnerTextViewSelectBeginDate.text.toString() + 'T' +
                binding.spinnerTextViewSelectBeginTime.text.toString() + ":00"
        val endDateTime = binding.spinnerTextViewSelectEndDate.text.toString() + 'T' +
                binding.spinnerTextViewSelectEndTime.text.toString() + ":00"
        val timeLimit = Duration.ofMinutes(binding.textExamTimeLimit.text.toString().toLong()).toString()
        val passScore = binding.textExamStandardScore.text.toString().toInt()

        val exam = Exam(selectedExam.id, app.loginUser.teacher?.id, examName, beginDateTime, endDateTime,
            timeLimit,passScore,false,false)

        viewModel.editExam(selectedExam.id!!,exam)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshExamInfo(exam: Exam){
        binding.textExamName.setText(exam.name)
        binding.textExamStandardScore.setText(exam.passScore.toString())
        binding.textExamTimeLimit.setText(translatePTFormat(exam.timeLimit!!))

        val beginDateTime = exam.beginDateTime?.split('T')
        binding.spinnerTextViewSelectBeginDate.setText(beginDateTime?.get(0))

        val beginTimeSplit = beginDateTime?.get(1)?.split(':')
        binding.spinnerTextViewSelectBeginTime.setText("${beginTimeSplit?.get(0)}:${beginTimeSplit?.get(1)}")

        val endDateTime = exam.endDateTime?.split('T')
        binding.spinnerTextViewSelectEndDate.setText(endDateTime?.get(0))

        val endTimeSplit = endDateTime?.get(1)?.split(':')
        binding.spinnerTextViewSelectEndTime.setText("${endTimeSplit?.get(0)}:${endTimeSplit?.get(1)}")
    }

    private fun editModeOn(){
        isEditMode = true
        binding.apply {
            textExamName.isEnabled = true
            textExamStandardScore.isEnabled = true
            textExamTimeLimit.isEnabled = true
            spinnerTextViewSelectBeginDate.isEnabled = true
            spinnerTextViewSelectBeginTime.isEnabled = true
            spinnerTextViewSelectEndDate.isEnabled = true
            spinnerTextViewSelectEndTime.isEnabled = true
            buttonEditExam.text = "저장하기"
        }
        Toast.makeText(requireContext(),"편집 모드가 활성화 되었습니다",Toast.LENGTH_SHORT).show()
    }

    private fun editModeOff(){
        isEditMode = false
        binding.apply {
            textExamName.isEnabled = false
            textExamStandardScore.isEnabled = false
            textExamTimeLimit.isEnabled = false
            spinnerTextViewSelectBeginDate.isEnabled = false
            spinnerTextViewSelectBeginTime.isEnabled = false
            spinnerTextViewSelectEndDate.isEnabled = false
            spinnerTextViewSelectEndTime.isEnabled = false
            buttonEditExam.text = "수정하기"
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun translatePTFormat(time: String): String{
        val duration = Duration.parse(time)
        return duration.toMinutes().toString()
    }
}