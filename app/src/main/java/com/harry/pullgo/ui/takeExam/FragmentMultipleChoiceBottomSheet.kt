package com.harry.pullgo.ui.takeExam

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
class FragmentMultipleChoiceBottomSheet(
    private val selectedQuestion: Question,
    private val onChoiceListener: OnChoiceListener
    ): BottomSheetDialogFragment() {
    private val binding by lazy{FragmentMultipleChoiceBottomSheetBinding.inflate(layoutInflater)}

    private val viewModel: TakeExamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setListeners()
        initViewModel()

        return binding.root
    }

    private fun setListeners(){
        binding.checkboxMultipleChoice1.setOnCheckedChangeListener{ buttonView, isChecked -> onChoiceListener.onChoice(1) }
        binding.checkboxMultipleChoice2.setOnCheckedChangeListener{ buttonView, isChecked -> onChoiceListener.onChoice(2) }
        binding.checkboxMultipleChoice3.setOnCheckedChangeListener{ buttonView, isChecked -> onChoiceListener.onChoice(3) }
        binding.checkboxMultipleChoice4.setOnCheckedChangeListener{ buttonView, isChecked -> onChoiceListener.onChoice(4) }
        binding.checkboxMultipleChoice5.setOnCheckedChangeListener{ buttonView, isChecked -> onChoiceListener.onChoice(5) }

        binding.buttonMultipleChoiceDone.setOnClickListener {
            dismiss()
        }
    }

    private fun initViewModel(){

    }
}