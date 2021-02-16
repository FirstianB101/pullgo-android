package com.harry.pullgo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

public class CalendarBottomSheetFragment extends BottomSheetDialogFragment
    implements View.OnClickListener {
    public static CalendarBottomSheetFragment getInstance(){return new CalendarBottomSheetFragment();}

    private LinearLayout emailLo;
    private LinearLayout dayInfo;
    private TextView dateTextView;
    private TextView testNumTextView;
    String date;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_calendar_bottom_sheet,container,false);

        emailLo=view.findViewById(R.id.emailLo);
        dayInfo=view.findViewById(R.id.LayoutDayInfo);
        dateTextView=view.findViewById(R.id.textViewShowDate);
        testNumTextView=view.findViewById(R.id.textViewTestNum);

        getDialog().setCanceledOnTouchOutside(true);

        Bundle dateBundle=getArguments();
        if(dateBundle!=null){
            date= dateBundle.getString("date");
            dateTextView.setText(date);
        }
        emailLo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.emailLo:
                new LessonInfoDialogFragment().show(
                        getChildFragmentManager(),LessonInfoDialogFragment.TAG_LESSON_INFO_DIALOG
                );
                return;
        }
        dismiss();
    }
}
