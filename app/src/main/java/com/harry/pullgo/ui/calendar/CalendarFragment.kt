package com.harry.pullgo.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.api.OnCalendarResetListener
import com.harry.pullgo.ui.lesson.FragmentCreateNewLessonDialog
import com.harry.pullgo.data.repository.LessonsRepository
import com.harry.pullgo.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.*

class CalendarFragment : Fragment(), OnDateSelectedListener {
    private val binding by lazy{FragmentCalendarBinding.inflate(layoutInflater)}

    private val viewModel: LessonsViewModel by viewModels{
        LessonsViewModelFactory(LessonsRepository(requireContext(),app.loginUser.token))
    }

    private val app: PullgoApplication by lazy { requireActivity().application as PullgoApplication }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        initializeCalendar()
        setViewModel()
        setListeners()

        return binding.root
    }

    private fun setViewModel(){
        viewModel.allLessonsRepositories.observe(requireActivity()){
            makeDots()
            app.dismissLoadingDialog()
        }

        if(app.loginUser.teacher != null){
            viewModel.requestTeacherLessons(app.loginUser.teacher?.id!!)
        }else if(app.loginUser.student != null){
            viewModel.requestStudentLessons(app.loginUser.student?.id!!)
        }

        app.showLoadingDialog(childFragmentManager)
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
                viewModel.requestTeacherLessons(app.loginUser?.teacher?.id!!)
                app.showLoadingDialog(childFragmentManager)
            }
        }
    }

    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
        val selectedDate = String.format("%04d-%02d-%02d", date.year, date.month + 1, date.day)
        val bottomSheet = FragmentCalendarBottomSheet(selectedDate)
        bottomSheet.calendarResetListenerListener = object: OnCalendarResetListener{
            override fun onResetCalendar() {
                viewModel.requestTeacherLessons(app.loginUser?.teacher?.id!!)
            }
        }
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