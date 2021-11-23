package com.harry.pullgo.ui.takenExamHistory

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
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
        val answers = selectedQuestion.answer!!

        binding.apply {
            textViewMultipleChoice1.text = choices["1"]
            textViewMultipleChoice2.text = choices["2"]
            textViewMultipleChoice3.text = choices["3"]
            textViewMultipleChoice4.text = choices["4"]
            textViewMultipleChoice5.text = choices["5"]

            if(studentAnswers?.contains(1) == true) {
                checkboxMultipleChoice1.isChecked = true
                changeLayoutColorToOrange(circleMultipleChoice1,linearLayoutMultipleChoice1,checkboxMultipleChoice1)
            }
            if(studentAnswers?.contains(2) == true) {
                checkboxMultipleChoice2.isChecked = true
                changeLayoutColorToOrange(circleMultipleChoice2,linearLayoutMultipleChoice2,checkboxMultipleChoice2)
            }
            if(studentAnswers?.contains(3) == true) {
                checkboxMultipleChoice3.isChecked = true
                changeLayoutColorToOrange(circleMultipleChoice3,linearLayoutMultipleChoice3,checkboxMultipleChoice3)
            }
            if(studentAnswers?.contains(4) == true) {
                checkboxMultipleChoice4.isChecked = true
                changeLayoutColorToOrange(circleMultipleChoice4,linearLayoutMultipleChoice4,checkboxMultipleChoice4)
            }
            if(studentAnswers?.contains(5) == true) {
                checkboxMultipleChoice5.isChecked = true
                changeLayoutColorToOrange(circleMultipleChoice5,linearLayoutMultipleChoice5,checkboxMultipleChoice5)
            }

            if(answers.contains(1)) changeLayoutColorToGreen(circleMultipleChoice1,linearLayoutMultipleChoice1,checkboxMultipleChoice1)
            if(answers.contains(2)) changeLayoutColorToGreen(circleMultipleChoice2,linearLayoutMultipleChoice2,checkboxMultipleChoice2)
            if(answers.contains(3)) changeLayoutColorToGreen(circleMultipleChoice3,linearLayoutMultipleChoice3,checkboxMultipleChoice3)
            if(answers.contains(4)) changeLayoutColorToGreen(circleMultipleChoice4,linearLayoutMultipleChoice4,checkboxMultipleChoice4)
            if(answers.contains(5)) changeLayoutColorToGreen(circleMultipleChoice5,linearLayoutMultipleChoice5,checkboxMultipleChoice5)
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

    private fun changeLayoutColorToGreen(numberText: TextView, layout: LinearLayout, checkBox: CheckBox){
        numberText.setBackgroundResource(R.drawable.circle_green)
        layout.setBackgroundResource(R.drawable.rounded_correct)
        checkBox.buttonTintList = ColorStateList.valueOf(resources.getColor(R.color.material_700_green))
    }

    private fun changeLayoutColorToOrange(numberText: TextView, layout: LinearLayout, checkBox: CheckBox){
        numberText.setBackgroundResource(R.drawable.circle_orange)
        layout.setBackgroundResource(R.drawable.rounded_wrong)
        checkBox.buttonTintList = ColorStateList.valueOf(resources.getColor(android.R.color.holo_orange_dark))
    }
}