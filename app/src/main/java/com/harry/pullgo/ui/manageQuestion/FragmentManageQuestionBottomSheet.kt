package com.harry.pullgo.ui.manageQuestion

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnEditMultipleChoiceListener
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.databinding.FragmentManageQuestionBottomSheetBinding
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
        binding.editTextManageMultipleChoice1.setText(choice?.get("1"))
        binding.editTextManageMultipleChoice2.setText(choice?.get("2"))
        binding.editTextManageMultipleChoice3.setText(choice?.get("3"))
        binding.editTextManageMultipleChoice4.setText(choice?.get("4"))
        binding.editTextManageMultipleChoice5.setText(choice?.get("5"))

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
        val choice = HashMap<String,String>()
        choice["1"] = binding.editTextManageMultipleChoice1.text.toString()
        choice["2"] = binding.editTextManageMultipleChoice2.text.toString()
        choice["3"] = binding.editTextManageMultipleChoice3.text.toString()
        choice["4"] = binding.editTextManageMultipleChoice4.text.toString()
        choice["5"] = binding.editTextManageMultipleChoice5.text.toString()

        val answer = mutableListOf<Int>()
        if(binding.checkboxManageMultipleChoice1.isChecked) answer.add(1)
        if(binding.checkboxManageMultipleChoice2.isChecked) answer.add(2)
        if(binding.checkboxManageMultipleChoice3.isChecked) answer.add(3)
        if(binding.checkboxManageMultipleChoice4.isChecked) answer.add(4)
        if(binding.checkboxManageMultipleChoice5.isChecked) answer.add(5)

        editMultipleChoiceListener.onEditMultipleChoice(choice,answer)
    }
}