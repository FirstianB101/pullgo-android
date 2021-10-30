package com.harry.pullgo.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.api.OnCalendarResetListener
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.FragmentCalendarBinding
import com.harry.pullgo.ui.lesson.FragmentCreateNewLessonDialog
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CalendarFragment : Fragment(), OnDateSelectedListener {
    private val binding by lazy{FragmentCalendarBinding.inflate(layoutInflater)}

    private val viewModel: LessonsViewModel by viewModels()

    @Inject
    lateinit var app: PullgoApplication

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        initializeCalendar()
        setViewModel()
        setListeners()

        return binding.root
    }

    private fun setViewModel(){
        viewModel.allLessonsRepositories.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    makeDots()
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"수업 정보를 불러올 수 없습니다(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        if(app.loginUser.teacher != null){
            viewModel.requestTeacherLessons(app.loginUser.teacher?.id!!)
        }else if(app.loginUser.student != null){
            viewModel.requestStudentLessons(app.loginUser.student?.id!!)
        }
    }

    private fun setListeners(){
        binding.floatingActionButtonCalendar.setOnClickListener {
            FragmentCreateNewLessonDialog().show(childFragmentManager,FragmentCreateNewLessonDialog.TAG_CREATE_NEW_LESSON_DIALOG)
        }

        if(app.loginUser.teacher == null)
            binding.floatingActionButtonCalendar.visibility = FloatingActionButton.GONE
    }

    private fun initializeCalendar(){
        binding.calendarView.removeDecorators()
        binding.calendarView.selectedDate = CalendarDay.today()

        binding.calendarView.addDecorators(
            CalendarSaturdayDecorator(),
            CalendarSundayDecorator(),
            CalendarTodayDecorator()
        )
        binding.calendarView.setOnDateChangedListener(this)

        setFragmentResultListener("isMadeNewLesson"){ _, bundle ->
            if(bundle.getString("isMade") == "yes"){
                viewModel.requestTeacherLessons(app.loginUser.teacher?.id!!)
            }
        }
    }

    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
        val selectedDate = String.format("%04d-%02d-%02d", date.year, date.month + 1, date.day)
        val bottomSheet = FragmentCalendarBottomSheet(selectedDate)
        bottomSheet.calendarResetListenerListener = object: OnCalendarResetListener{
            override fun onResetCalendar() {
                viewModel.requestTeacherLessons(app.loginUser.teacher?.id!!)
            }
        }
        bottomSheet.show(childFragmentManager, "bottomSheetTestList")
    }

    private fun makeDots(){
        val lessons = viewModel.allLessonsRepositories.value?.data
        val dates = ArrayList<String>()
        val calList = ArrayList<CalendarDay>()

        if (lessons != null) {
            for(les in lessons){
                dates.add(les.schedule?.date!!)
            }
        }

        var tmp: List<Int>
        for(date in dates){
            //연도-월-일 형식이며 월은 0부터 시작하므로 1 빼줌
            tmp = date.split('-').map{ d->d.toInt() }
            calList.add(CalendarDay.from(tmp[0],tmp[1] - 1,tmp[2]))
        }

        binding.calendarView.removeDecorators()
        binding.calendarView.addDecorators(
            CalendarEventDecorator(R.color.statusbar_color,calList),
            CalendarSaturdayDecorator(),
            CalendarSundayDecorator(),
            CalendarTodayDecorator()
        )
    }
}