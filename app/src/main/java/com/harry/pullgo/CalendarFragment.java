package com.harry.pullgo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Collections;

public class CalendarFragment extends Fragment {
    MaterialCalendarView calendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_calendar,container,false);

        calendarView=rootView.findViewById(R.id.calendarView);
        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.addDecorators(new CalendarSaturdayDecorator(),new CalendarSundayDecorator(),new TodayDecorator());

        calendarView.addDecorator(new CalendarEventDecorator(Color.RED, Collections.singleton(CalendarDay.today())));

        return rootView;
    }

    class TodayDecorator implements DayViewDecorator {
        private CalendarDay date;

        public TodayDecorator(){date=CalendarDay.today();}

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new RelativeSizeSpan(1.2f));
            view.addSpan(new ForegroundColorSpan(Color.GREEN));
        }
    }
}