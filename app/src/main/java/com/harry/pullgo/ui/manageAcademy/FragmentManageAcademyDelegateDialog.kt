package com.harry.pullgo.ui.manageAcademy

import android.R
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.databinding.DialogManageAcademyDelegateBinding
import com.harry.pullgo.ui.teacherFragment.TeacherManageAcademyViewModel

class FragmentManageAcademyDelegateDialog(private val selectedAcademy: Academy): DialogFragment() {
    private val binding by lazy{DialogManageAcademyDelegateBinding.inflate(layoutInflater)}

    private val viewModel: TeacherManageAcademyViewModel by activityViewModels()

    private lateinit var selectedTeacher: Teacher

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        initViewModel()
        setListeners()
        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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
        val teachers = viewModel.teachersAtAcademyRepository.value!!

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