package com.ich.pullgo.ui.manageAcademy

import android.R
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/manageAcademy/FragmentManageAcademyDelegateDialog.kt
import com.ich.pullgo.data.models.Academy
import com.ich.pullgo.data.models.Teacher
import com.ich.pullgo.databinding.DialogManageAcademyDelegateBinding
=======
import com.ich.pullgo.databinding.DialogManageAcademyDelegateBinding
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Teacher
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/manageAcademy/FragmentManageAcademyDelegateDialog.kt
import com.ich.pullgo.ui.teacherFragment.TeacherManageAcademyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentManageAcademyDelegateDialog(private val selectedAcademy: Academy): DialogFragment() {
    private val binding by lazy{DialogManageAcademyDelegateBinding.inflate(layoutInflater)}

    private val viewModel: TeacherManageAcademyViewModel by activityViewModels()

    private lateinit var selectedTeacher: Teacher

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        initViewModel()
        setListeners()
        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        _dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return _dialog
    }

    private fun initViewModel(){
        viewModel.teachersAtAcademyRepository.observe(requireActivity()){
            setSpinner()
        }

        viewModel.getTeachersAtAcademy(selectedAcademy.id!!)
    }

    private fun setListeners(){
        binding.buttonManageAcademyDoDelegate.setOnClickListener {

        }

        binding.buttonManageAcademyCancelDelegate.setOnClickListener {
            dismiss()
        }
    }

    private fun setSpinner(){
        val teachers = viewModel.teachersAtAcademyRepository.value?.data!!

        val adapter: ArrayAdapter<Teacher> = ArrayAdapter(requireContext(),
            R.layout.simple_spinner_dropdown_item,teachers)
        binding.spinnerManageAcademyDelegate.adapter = adapter

        binding.spinnerManageAcademyDelegate.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedTeacher = teachers[position]
                Toast.makeText(requireContext(),"선택: ${selectedTeacher.account?.fullName}",Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    companion object {
        const val TAG_MANAGE_ACADEMY_DELEGATE_DIALOG = "manage_academy_delegate_dialog"
    }
}