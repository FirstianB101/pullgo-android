package com.harry.pullgo.ui.ChangePersonInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.harry.pullgo.R;

public class ChangePersonInfoFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_student_change_info, container, false);
        final TextView textView = root.findViewById(R.id.text_ChangeInfo);

        textView.setText("회원정보 수정 페이지");

        return root;
    }
}