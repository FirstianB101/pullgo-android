package com.harry.pullgo.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harry.pullgo.databinding.FragmentCalendarBottomSheetBinding
import com.harry.pullgo.ui.FragmentLessonInfoDialog

class FragmentCalendarBottomSheet : BottomSheetDialogFragment(){
    private val binding by lazy{FragmentCalendarBottomSheetBinding.inflate(layoutInflater)}

    var date: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initialize()
        setListeners()

        return binding.root
    }

    private fun initialize(){
        dialog!!.setCanceledOnTouchOutside(true)
        val dateBundle = arguments
        if (dateBundle != null) {
            date = dateBundle.getString("date")
            binding.textViewShowDate.text = date
        }
    }

    private fun setListeners(){
        binding.emailLo.setOnClickListener {
            FragmentLessonInfoDialog().show(childFragmentManager, FragmentLessonInfoDialog.TAG_LESSON_INFO_DIALOG)
            dismiss()
        }
    }
}