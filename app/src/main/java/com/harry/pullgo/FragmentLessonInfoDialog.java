package com.harry.pullgo;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

public class FragmentLessonInfoDialog extends DialogFragment {

    public static final String TAG_LESSON_INFO_DIALOG="lesson_info_dialog";
    public static FragmentLessonInfoDialog getInstance(){
        return new FragmentLessonInfoDialog();
    }

    private TextView academyName;
    private TextView lessonName;
    private TextView lessonTime;
    private TextView teacherName;

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.fragment_lesson_info_dialog,null);

        academyName=view.findViewById(R.id.textViewAcademyName);
        lessonName=view.findViewById(R.id.textViewLessonName);
        lessonTime=view.findViewById(R.id.textViewLessonTime);
        teacherName=view.findViewById(R.id.textViewTeacherName);

        setLessonInformation();

        builder .setView(view)
                .setPositiveButton("확인", (dialog, which) -> {
                   dismiss();
                });

        return builder.create();
    }

    private void setLessonInformation(){
        //이곳에서 서버에서 받아온 수업정보 텍스트뷰에 적용
    }
}