package com.ich.pullgo.presentation.main.common.components.calendar_screen.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*

class CalendarEventDecorator(private val color: Int, dates: Collection<CalendarDay>?) : DayViewDecorator {
    private val dates: HashSet<CalendarDay> = HashSet(dates)
    
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(8F, color))
    }

}