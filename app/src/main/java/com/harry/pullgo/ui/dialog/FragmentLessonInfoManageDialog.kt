package com.harry.pullgo.ui.dialog

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnCalendarReset
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Lesson
import com.harry.pullgo.data.objects.Schedule
import com.harry.pullgo.data.repository.LessonsRepository
import com.harry.pullgo.databinding.DialogLessonInfoManageBinding
import com.harry.pullgo.ui.calendar.LessonsViewModel
import com.harry.pullgo.ui.calendar.LessonsViewModelFactory
import com.harry.pullgo.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class FragmentLessonInfoManageDialog(private val selectedLesson: Lesson) :DialogFragment(){
    private val binding by lazy{DialogLessonInfoManageBinding.inflate(layoutInflater)}
    private var selectedDate: Long? = null
    private var startHour = selectedLesson.schedule?.beginTime!!.split(':')[0].toInt()
    private var startMinute = selectedLesson.schedule?.beginTime!!.split(':')[1].toInt()
    private var endHour = selectedLesson.schedule?.endTime!!.split(':')[0].toInt()
    private var endMinute = selectedLesson.schedule?.endTime!!.split(':')[1].toInt()
    private var isEditModeOn = false

    lateinit var viewModel: LessonsViewModel

    var calendarResetListener: OnCalendarReset? = null

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
            binding.textViewLessonInfoManageSelectStartTime.setText(String.format("%02d:%02d:00",startHour,startMinute))
            resetIfTimeNotAppropriate()
        }

        endTimePicker.addOnPositiveButtonClickListener {
            endHour = endTimePicker.hour
            endMinute = endTimePicker.minute
            binding.textViewLessonInfoManageSelectEndTime.setText(String.format("%02d:%02d:00",endHour,endMinute))
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
            changeLayoutIfEditModeOn()
        }

        binding.buttonLessonInfoManageRemove.setOnClickListener {
            showRemoveDialog()
        }
    }

    private fun changeLayoutIfEditModeOn(){
        if(isEditModeOn){
            binding.textLessonInfoManageLessonName.isEnabled = false
            binding.textViewLessonInfoManageSelectDate.isEnabled = false
            binding.textViewLessonInfoManageSelectStartTime.isEnabled = false
            binding.textViewLessonInfoManageSelectEndTime.isEnabled = false

            binding.buttonLessonInfoManageSave.text = resources.getText(R.string.underlined_edit)
            isEditModeOn = false

            requestPatchLesson()
        }else{
            binding.textLessonInfoManageLessonName.isEnabled = true
            binding.textViewLessonInfoManageSelectDate.isEnabled = true
            binding.textViewLessonInfoManageSelectStartTime.isEnabled = true
            binding.textViewLessonInfoManageSelectEndTime.isEnabled = true

            binding.buttonLessonInfoManageSave.text = resources.getText(R.string.underlined_edit_success)

            binding.textLessonInfoManageLessonName.requestFocus()

            Toast.makeText(requireContext(),"편집 모드로 전환되었습니다",Toast.LENGTH_SHORT).show()
            isEditModeOn = true
        }
    }

    private fun requestPatchLesson(){
        val client = RetrofitClient.getApiService()

        selectedLesson.name = binding.textLessonInfoManageLessonName.text.toString()

        val schedule = Schedule(null,null,null)
        schedule.date = binding.textViewLessonInfoManageSelectDate.text.toString()
        schedule.beginTime = binding.textViewLessonInfoManageSelectStartTime.text.toString()
        schedule.endTime = binding.textViewLessonInfoManageSelectEndTime.text.toString()
        selectedLesson.schedule = schedule

        client.patchLessonInfo(selectedLesson.id!!,selectedLesson).enqueue(object: Callback<Lesson> {
            override fun onResponse(call: Call<Lesson>, response: Response<Lesson>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),"수업 정보가 변경되었습니다",Toast.LENGTH_SHORT).show()
                    calendarResetListener?.onResetCalendar()
                    parentFragment?.setFragmentResult("isLessonPatched", bundleOf("Patched" to "yes"))
                    dismiss()
                }else{
                    Toast.makeText(requireContext(),"수업 정보가 변경에 실패했습니다",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Lesson>, t: Throwable) {
                Toast.makeText(requireContext(),"서버와 연결에 실패했습니다",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRemoveDialog(){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                requestRemoveLesson()
            }
        }
        dialog.start("수업 삭제","${selectedLesson.name} 수업을 삭제하시겠습니까?","삭제하기","취소")
    }

    private fun requestRemoveLesson(){
        val client = RetrofitClient.getApiService()
        
        client.deleteLesson(selectedLesson.id!!).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),"수업이 삭제되었습니다",Toast.LENGTH_SHORT).show()
                    calendarResetListener?.onResetCalendar()
                    parentFragment?.setFragmentResult("isLessonPatched", bundleOf("Patched" to "yes"))
                    dismiss()
                }else{
                    Toast.makeText(requireContext(),"수업 삭제에 실패했습니다",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(requireContext(),"서버와 연결에 실패했습니다",Toast.LENGTH_SHORT).show()
            }
        })
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
            startHour = selectedLesson.schedule?.beginTime!!.split(':')[0].toInt()
            startMinute = selectedLesson.schedule?.beginTime!!.split(':')[1].toInt()
            endHour = selectedLesson.schedule?.endTime!!.split(':')[0].toInt()
            endMinute = selectedLesson.schedule?.endTime!!.split(':')[1].toInt()

            binding.textViewLessonInfoManageSelectStartTime.setText(selectedLesson.schedule?.beginTime!!.toString())
            binding.textViewLessonInfoManageSelectEndTime.setText(selectedLesson.schedule?.endTime!!.toString())
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