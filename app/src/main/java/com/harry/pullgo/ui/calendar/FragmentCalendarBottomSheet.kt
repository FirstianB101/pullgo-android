package com.harry.pullgo.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harry.pullgo.R
import com.harry.pullgo.databinding.FragmentCalendarBottomSheetBinding
import com.harry.pullgo.ui.FragmentLessonInfoDialog

class FragmentCalendarBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {
    private val binding by lazy{FragmentCalendarBottomSheetBinding.inflate(layoutInflater)}

    var date: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.setCanceledOnTouchOutside(true)
        val dateBundle = arguments
        if (dateBundle != null) {
            date = dateBundle.getString("date")
            binding.textViewShowDate.text = date
        }
        binding.emailLo.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.emailLo -> {
                FragmentLessonInfoDialog().show(
                    childFragmentManager, FragmentLessonInfoDialog.TAG_LESSON_INFO_DIALOG
                )
                return
            }
        }
        dismiss()
    }
}