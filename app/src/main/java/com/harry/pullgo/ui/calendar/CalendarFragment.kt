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
import com.harry.pullgo.ui.lesson.CreateNewLessonActivity
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.*

//https://cnwlcjf.tistory.com/68
class CalendarFragment : Fragment(), OnDateSelectedListener {
    private val binding by lazy{FragmentCalendarBinding.inflate(layoutInflater)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initializeCalendar()
        setListeners()
        return binding.root
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
            CalendarSundayDecorator(),
            TodayDecorator()
        )
        binding.calendarView.addDecorator(CalendarEventDecorator(Color.RED, setOf(CalendarDay.today())))
        binding.calendarView.setOnDateChangedListener(this)
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        val selectedDate = String.format("%d년 %d월 %d일", date.year, date.month + 1, date.day)
        val bottomSheet = FragmentCalendarBottomSheet()
        val bundle = Bundle()
        bundle.putString("date", selectedDate)
        bottomSheet.arguments = bundle
        bottomSheet.show(childFragmentManager, "bottomSheetTestList")
    }

    internal inner class TodayDecorator : DayViewDecorator {
        private val date: CalendarDay = CalendarDay.today()

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == date
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(RelativeSizeSpan(1.2f))
            view.addSpan(ForegroundColorSpan(Color.GREEN))
        }

    }
}