package com.harry.pullgo.calendar;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harry.pullgo.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
//https://cnwlcjf.tistory.com/68
public class CalendarFragment extends Fragment implements OnDateSelectedListener {
    MaterialCalendarView calendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_calendar,container,false);

        calendarView=rootView.findViewById(R.id.calendarView);
        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.addDecorators(new CalendarSaturdayDecorator(),new CalendarSundayDecorator(),new TodayDecorator());
        calendarView.addDecorator(new CalendarEventDecorator(Color.RED, Collections.singleton(CalendarDay.today())));

        calendarView.setOnDateChangedListener(this);
        return rootView;
    }

    @Override
    public void onDateSelected(@NonNull @NotNull MaterialCalendarView widget, @NonNull @NotNull CalendarDay date, boolean selected) {
        String selectedDate=String.format("%d년 %d월 %d일",date.getYear(),date.getMonth()+1,date.getDay());

        FragmentCalendarBottomSheet bottomSheet= FragmentCalendarBottomSheet.getInstance();
        Bundle bundle=new Bundle();
        bundle.putString("date",selectedDate);
        bottomSheet.setArguments(bundle);
        bottomSheet.show(getChildFragmentManager(),"bottomSheetTestList");
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