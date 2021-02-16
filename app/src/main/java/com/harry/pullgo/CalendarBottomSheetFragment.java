package com.harry.pullgo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class CalendarBottomSheetFragment extends BottomSheetDialogFragment
    implements View.OnClickListener {
    public static CalendarBottomSheetFragment getInstance(){return new CalendarBottomSheetFragment();}

    private LinearLayout msgLo;
    private LinearLayout emailLo;
    private LinearLayout cloudLo;
    private LinearLayout bluetoothLo;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_calendar_bottom_sheet,container,false);
        msgLo=view.findViewById(R.id.msgLo);
        emailLo=view.findViewById(R.id.emailLo);
        cloudLo=view.findViewById(R.id.cloudLo);
        bluetoothLo=view.findViewById(R.id.bluetoothLo);
        getDialog().setCanceledOnTouchOutside(true);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.msgLo:
                Toast.makeText(getContext(),"Menu1",Toast.LENGTH_SHORT).show();
                break;
            case R.id.emailLo:
                Toast.makeText(getContext(),"Menu2",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cloudLo:
                Toast.makeText(getContext(),"Menu3",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bluetoothLo:
                Toast.makeText(getContext(),"Menu4",Toast.LENGTH_SHORT).show();
                break;
        }
        dismiss();
    }
}
