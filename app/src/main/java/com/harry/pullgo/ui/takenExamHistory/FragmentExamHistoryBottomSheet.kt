package com.harry.pullgo.ui.takenExamHistory

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harry.pullgo.R
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.databinding.FragmentMultipleChoiceBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentExamHistoryBottomSheet(
    private val selectedQuestion: Question,
    private val studentAnswers: List<Int>?
    ): BottomSheetDialogFragment() {
    private val binding by lazy{FragmentMultipleChoiceBottomSheetBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.setCanceledOnTouchOutside(false)

        binding.textViewMultipleChoiceMent.text = "보기 확인"

        disableCheckboxes()

        val choices = selectedQuestion.choice!!
        binding.apply {
            textViewMultipleChoice1.text = choices["1"]
            textViewMultipleChoice2.text = choices["2"]
            textViewMultipleChoice3.text = choices["3"]
            textViewMultipleChoice4.text = choices["4"]
            textViewMultipleChoice5.text = choices["5"]
        }

        val answers = selectedQuestion.answer!!
        binding.apply {
            checkImageMultipleChoice1.visibility = if(answers.contains(1)) View.VISIBLE else View.GONE
            checkImageMultipleChoice2.visibility = if(answers.contains(2)) View.VISIBLE else View.GONE
            checkImageMultipleChoice3.visibility = if(answers.contains(3)) View.VISIBLE else View.GONE
            checkImageMultipleChoice4.visibility = if(answers.contains(4)) View.VISIBLE else View.GONE
            checkImageMultipleChoice5.visibility = if(answers.contains(5)) View.VISIBLE else View.GONE
        }

        binding.apply{
            if(studentAnswers?.contains(1) == true) checkboxMultipleChoice1.isChecked = true
            if(studentAnswers?.contains(2) == true) checkboxMultipleChoice2.isChecked = true
            if(studentAnswers?.contains(3) == true) checkboxMultipleChoice3.isChecked = true
            if(studentAnswers?.contains(4) == true) checkboxMultipleChoice4.isChecked = true
            if(studentAnswers?.contains(5) == true) checkboxMultipleChoice5.isChecked = true
        }

        binding.buttonMultipleChoiceDone.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    private fun disableCheckboxes(){
        binding.apply {
            checkboxMultipleChoice1.isEnabled = false
            checkboxMultipleChoice2.isEnabled = false
            checkboxMultipleChoice3.isEnabled = false
            checkboxMultipleChoice4.isEnabled = false
            checkboxMultipleChoice5.isEnabled = false
        }
    }
}