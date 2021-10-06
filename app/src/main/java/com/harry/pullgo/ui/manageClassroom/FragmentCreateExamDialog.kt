package com.harry.pullgo.ui.manageClassroom

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.databinding.DialogCreateExamBinding

class FragmentCreateExamDialog(private val selectedClassroom: Classroom): DialogFragment() {
    private val binding by lazy{DialogCreateExamBinding.inflate(layoutInflater)}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())

        setListeners()

        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        _dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return _dialog
    }

    private fun setListeners(){
        binding.buttonCreateNewExam.setOnClickListener {

        }

        binding.buttonCancelCreateExam.setOnClickListener {
            dismiss()
        }
    }
}