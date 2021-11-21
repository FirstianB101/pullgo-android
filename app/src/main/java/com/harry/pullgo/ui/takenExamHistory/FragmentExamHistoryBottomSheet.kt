package com.harry.pullgo.ui.takenExamHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnChoiceListener
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.databinding.FragmentMultipleChoiceBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentExamHistoryBottomSheet(
    private val selectedQuestion: Question,
    private val currentAnswer: List<Int>?,
    private val onChoiceListener: OnChoiceListener
    ): BottomSheetDialogFragment() {
    private val binding by lazy{FragmentMultipleChoiceBottomSheetBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.setCanceledOnTouchOutside(false)

        initialize()
        setListeners()

        return binding.root
    }

    private fun initialize(){
        val choices = selectedQuestion.choice!!
        binding.apply {
            textViewMultipleChoice1.text = choices["1"]
            textViewMultipleChoice2.text = choices["2"]
            textViewMultipleChoice3.text = choices["3"]
            textViewMultipleChoice4.text = choices["4"]
            textViewMultipleChoice5.text = choices["5"]
        }

        if(currentAnswer != null){
            binding.apply {
                if(currentAnswer.contains(1)) checkboxMultipleChoice1.isChecked = true
                if(currentAnswer.contains(2)) checkboxMultipleChoice2.isChecked = true
                if(currentAnswer.contains(3)) checkboxMultipleChoice3.isChecked = true
                if(currentAnswer.contains(4)) checkboxMultipleChoice4.isChecked = true
                if(currentAnswer.contains(5)) checkboxMultipleChoice5.isChecked = true
            }
        }
    }

    private fun setListeners(){
        binding.buttonMultipleChoiceDone.setOnClickListener {
            saveAnswer()
            dismiss()
        }
    }

    private fun saveAnswer(){
        val answers = mutableListOf<Int>()
        binding.apply {
            if(checkboxMultipleChoice1.isChecked) answers.add(1)
            if(checkboxMultipleChoice2.isChecked) answers.add(2)
            if(checkboxMultipleChoice3.isChecked) answers.add(3)
            if(checkboxMultipleChoice4.isChecked) answers.add(4)
            if(checkboxMultipleChoice5.isChecked) answers.add(5)
        }
        onChoiceListener.onChoice(answers)
    }
}