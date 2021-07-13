package com.harry.pullgo.ui.lesson

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.harry.pullgo.R
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.*
import com.harry.pullgo.data.repository.CreateNewLessonRepository
import com.harry.pullgo.databinding.ActivityCreateNewLessonBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class CreateNewLessonActivity : AppCompatActivity() {
    val binding by lazy {ActivityCreateNewLessonBinding.inflate(layoutInflater)}
    private var selectedDate: Long? = null
    private var startHour = -1
    private var startMinute = -1
    private var endHour = -1
    private var endMinute = -1
    private var selectedClassroom: Classroom? = null

    private lateinit var viewModel: CreateNewLessonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        initViewModel()
    }

    private fun initViewModel(){
        val viewModelFactory = CreateNewLessonViewModelFactory(CreateNewLessonRepository())
        viewModel = ViewModelProvider(this,viewModelFactory).get(CreateNewLessonViewModel::class.java)

        viewModel.createNewLessonRepositories.observe(this){
            setSpinnerItems()
        }
        viewModel.requestGetClassrooms(LoginInfo.loginTeacher?.id!!)
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
            datePicker.show(supportFragmentManager,"date")
        }

        binding.spinnerTextViewSelectStartTime.setOnClickListener {
            startTimePicker.show(supportFragmentManager,"startTime")
        }

        binding.spinnerTextViewSelectEndTime.setOnClickListener {
            endTimePicker.show(supportFragmentManager,"endTime")
        }

        binding.buttonCreateNewLesson.setOnClickListener {
            val beginTime = String.format("%02d:%02d:00",startHour,startMinute)
            val endTime = String.format("%02d:%02d:00",endHour,endMinute)
            val date = MillToDate(selectedDate!!)
            val schedule = Schedule(date,beginTime,endTime)
            val newLesson = Lesson(binding.textNewLessonName.text.toString(),selectedClassroom?.id,schedule)
            createLesson(newLesson)
            finish()
        }
    }

    private fun setSpinnerItems(){
        val classrooms = viewModel.createNewLessonRepositories.value!!.toMutableList()
        val adapter: ArrayAdapter<Classroom> = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,classrooms)
        binding.spinnerSelectClassroom.setAdapter(adapter)
        selectedClassroom = if(classrooms.isEmpty()) null else classrooms[0]

        binding.spinnerSelectClassroom.setOnItemSelectedListener { view, position, id, item ->
            selectedClassroom = item as Classroom
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

    private fun createLesson(lesson: Lesson){
        val service= RetrofitClient.getApiService()

        service.createLesson(lesson).enqueue(object: Callback<Lesson> {
            override fun onResponse(call: Call<Lesson>, response: Response<Lesson>) {
                if(response.isSuccessful){
                    val lesson = response.body()
                    Toast.makeText(applicationContext,"수업을 생성하였습니다",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext,"수업을 생성하지 못했습니다",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Lesson>, t: Throwable) {
                Toast.makeText(applicationContext,"서버와 연결에 실패했습니다",Toast.LENGTH_SHORT).show()
            }
        })
    }
}