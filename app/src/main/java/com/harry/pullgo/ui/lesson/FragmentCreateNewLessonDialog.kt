package com.harry.pullgo.ui.lesson

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.harry.pullgo.R
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Lesson
import com.harry.pullgo.data.models.Schedule
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.ClassroomsRepository
import com.harry.pullgo.databinding.DialogCreateNewLessonBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class FragmentCreateNewLessonDialog : DialogFragment() {
    val binding by lazy {DialogCreateNewLessonBinding.inflate(layoutInflater)}
    private var selectedDate: Long? = null
    private var startHour = -1
    private var startMinute = -1
    private var endHour = -1
    private var endMinute = -1
    private var selectedClassroom: Classroom? = null

    private val viewModel: CreateNewLessonViewModel by viewModels{CreateNewLessonViewModelFactory(ClassroomsRepository(requireContext()))}

    private var isLayoutVisible = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())

        initialize()
        initViewModel()

        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return _dialog
    }

    private fun initViewModel(){
        viewModel.createNewLessonClassroomRepository.observe(this){
            setSpinnerItems()
        }

        viewModel.createMessage.observe(requireActivity()){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            if(it == "수업을 생성하였습니다"){
                parentFragment?.setFragmentResult("isMadeNewLesson", bundleOf("isMade" to "yes"))
                dismiss()
            }
        }

        viewModel.requestGetClassrooms(LoginInfo.user?.teacher?.id!!)
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
                binding.spinnerTextViewSelectDate.setText(MillToDate(selectedDate!!).toString())
            }else{
                Snackbar.make(binding.root,"과거 날짜는 선택될 수 없습니다",Snackbar.LENGTH_SHORT).show()
            }
        }

        startTimePicker.addOnPositiveButtonClickListener {
            startHour = startTimePicker.hour
            startMinute = startTimePicker.minute
            binding.spinnerTextViewSelectStartTime.setText("${startHour}시 ${startMinute}분 ~")
            resetIfTimeNotAppropriate()
        }

        endTimePicker.addOnPositiveButtonClickListener {
            endHour = endTimePicker.hour
            endMinute = endTimePicker.minute
            binding.spinnerTextViewSelectEndTime.setText("${endHour}시 ${endMinute}분 까지")
            resetIfTimeNotAppropriate()
        }

        binding.spinnerTextViewSelectDate.setOnClickListener {
            datePicker.show(childFragmentManager,"date")
        }

        binding.spinnerTextViewSelectStartTime.setOnClickListener {
            startTimePicker.show(childFragmentManager,"startTime")
        }

        binding.spinnerTextViewSelectEndTime.setOnClickListener {
            endTimePicker.show(childFragmentManager,"endTime")
        }

        binding.buttonCreateNewLesson.setOnClickListener {
            if(isSelectedAllOptions()) {
                val beginTime = String.format("%02d:%02d:00", startHour, startMinute)
                val endTime = String.format("%02d:%02d:00", endHour, endMinute)
                val date = MillToDate(selectedDate!!)
                val schedule = Schedule(date, beginTime, endTime)
                val newLesson = Lesson(
                    binding.textNewLessonName.text.toString(),
                    selectedClassroom?.id,
                    schedule
                )
                viewModel.createNewLesson(newLesson)
            }else{
                Toast.makeText(requireContext(),"선택하지 않은 항목이 존재합니다",Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonCancelCreateNewLesson.setOnClickListener { dismiss() }
    }

    private fun setSpinnerItems(){
        val classrooms = viewModel.createNewLessonClassroomRepository.value!!
        val adapter: ArrayAdapter<Classroom> = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,classrooms)
        binding.spinnerSelectClassroom.adapter = adapter

        binding.spinnerSelectClassroom.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedClassroom = classrooms[position]
                if(!isLayoutVisible)isLayoutVisible = true
                changeLayoutVisibility()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                isLayoutVisible = false
                changeLayoutVisibility()
            }
        }
    }

    private fun changeLayoutVisibility(){
        if(isLayoutVisible){
            val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha)
            binding.layoutCreateNewLesson.visibility = View.VISIBLE
            binding.layoutCreateNewLesson.startAnimation(anim)
        }else{
            binding.layoutCreateNewLesson.visibility = View.GONE
        }
    }


    private fun resetIfTimeNotAppropriate(){
        if(startHour == -1 || startMinute == -1 || endHour == -1 || endMinute == -1)return

        if(startHour > endHour || (startHour == endHour && startMinute > endMinute)){
            startHour = -1
            startMinute = -1
            endHour = -1
            endMinute = -1
            binding.spinnerTextViewSelectStartTime.setText("")
            binding.spinnerTextViewSelectEndTime.setText("")
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
        const val TAG_CREATE_NEW_LESSON_DIALOG = "create_new_lesson_dialog"
    }
}