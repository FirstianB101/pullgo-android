package com.harry.pullgo.ui.calendar

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.harry.pullgo.R
import com.harry.pullgo.ui.lesson.CreateNewLessonActivity
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.LessonsRepository
import com.harry.pullgo.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.*

class CalendarFragment : Fragment(), OnDateSelectedListener {
    private val binding by lazy{FragmentCalendarBinding.inflate(layoutInflater)}

    private lateinit var viewModel: LessonsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initializeCalendar()
        setViewModel()
        setListeners()
        return binding.root
    }

    private fun setViewModel(){
        viewModel = LessonsViewModel(LessonsRepository())

        viewModel.allLessonsRepositories.observe(requireActivity()){
            makeDots()
        }

        if(LoginInfo.loginTeacher != null){
            viewModel.requestTeacherLessons(LoginInfo.loginTeacher?.id!!)
        }else if(LoginInfo.loginStudent != null){
            viewModel.requestStudentLessons(LoginInfo.loginStudent?.id!!)
        }
    }

    private fun setListeners(){
        binding.floatingActionButtonCalendar.setOnClickListener {
            val intent = Intent(requireContext(), CreateNewLessonActivity::class.java)
            startActivity(intent)
        }

        if(LoginInfo.loginTeacher == null){
            binding.floatingActionButtonCalendar.visibility = FloatingActionButton.GONE
        }
    }

    private fun initializeCalendar(){
        binding.calendarView.selectedDate = CalendarDay.today()
        binding.calendarView.addDecorators(
            CalendarSaturdayDecorator(),
            CalendarSundayDecorator()
        )
        binding.calendarView.addDecorator(CalendarEventDecorator(Color.RED, setOf(CalendarDay.today())))
        binding.calendarView.setOnDateChangedListener(this)
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        val selectedDate = String.format("%04d-%02d-%02d", date.year, date.month + 1, date.day)
        val bottomSheet = FragmentCalendarBottomSheet()
        val bundle = Bundle()
        bundle.putString("date", selectedDate)
        bottomSheet.arguments = bundle
        bottomSheet.show(childFragmentManager, "bottomSheetTestList")
    }

    private fun makeDots(){
        val lessons = viewModel.allLessonsRepositories.value
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
            tmp = date.split('-').map{d->d.toInt()}
            calList.add(CalendarDay.from(tmp[0],tmp[1] - 1,tmp[2]))
        }

        for(cday in calList){
            binding.calendarView.addDecorator(CalendarEventDecorator(R.color.secondary_color,calList))
        }
    }
}