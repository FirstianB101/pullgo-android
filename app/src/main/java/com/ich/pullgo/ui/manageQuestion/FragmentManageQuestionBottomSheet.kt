package com.ich.pullgo.ui.manageQuestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ich.pullgo.data.api.OnEditMultipleChoiceListener
import com.ich.pullgo.databinding.FragmentManageQuestionBottomSheetBinding
import com.ich.pullgo.domain.model.Question
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentManageQuestionBottomSheet(
    private val selectedQuestion: Question,
    private val editMultipleChoiceListener: OnEditMultipleChoiceListener
): BottomSheetDialogFragment() {
    private val binding by lazy{FragmentManageQuestionBottomSheetBinding.inflate(layoutInflater)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.setCanceledOnTouchOutside(false)

        initialize()
        setListeners()

        return binding.root
    }

    private fun initialize(){
        val choice = selectedQuestion.choice
        binding.apply {
            editTextManageMultipleChoice1.setText(choice?.get("1"))
            editTextManageMultipleChoice2.setText(choice?.get("2"))
            editTextManageMultipleChoice3.setText(choice?.get("3"))
            editTextManageMultipleChoice4.setText(choice?.get("4"))
            editTextManageMultipleChoice5.setText(choice?.get("5"))
        }

        val answer = selectedQuestion.answer
        if(answer != null){
            if(answer.contains(1))binding.checkboxManageMultipleChoice1.isChecked = true
            if(answer.contains(2))binding.checkboxManageMultipleChoice2.isChecked = true
            if(answer.contains(3))binding.checkboxManageMultipleChoice3.isChecked = true
            if(answer.contains(4))binding.checkboxManageMultipleChoice4.isChecked = true
            if(answer.contains(5))binding.checkboxManageMultipleChoice5.isChecked = true
        }
    }

    private fun setListeners(){
        binding.buttonManageMultipleChoiceDone.setOnClickListener {
            saveChoices()
            dismiss()
        }
    }

    private fun saveChoices(){
        val choice = HashMap<String, String>()
        val answer = mutableListOf<Int>()

        binding.apply {
            choice["1"] = editTextManageMultipleChoice1.text.toString()
            choice["2"] = editTextManageMultipleChoice2.text.toString()
            choice["3"] = editTextManageMultipleChoice3.text.toString()
            choice["4"] = editTextManageMultipleChoice4.text.toString()
            choice["5"] = editTextManageMultipleChoice5.text.toString()

            if (checkboxManageMultipleChoice1.isChecked) answer.add(1)
            if (checkboxManageMultipleChoice2.isChecked) answer.add(2)
            if (checkboxManageMultipleChoice3.isChecked) answer.add(3)
            if (checkboxManageMultipleChoice4.isChecked) answer.add(4)
            if (checkboxManageMultipleChoice5.isChecked) answer.add(5)
        }

        editMultipleChoiceListener.onEditMultipleChoice(choice,answer)
    }
}