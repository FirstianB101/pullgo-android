package com.ich.pullgo.ui.manageClassroom

import android.R
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ich.pullgo.application.PullgoApplication
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/manageClassroom/FragmentCreateClassroomDialog.kt
import com.ich.pullgo.data.models.Academy
import com.ich.pullgo.data.models.Classroom
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.DialogCreateClassroomBinding
=======
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.DialogCreateClassroomBinding
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/manageClassroom/FragmentCreateClassroomDialog.kt
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FragmentCreateClassroomDialog(private val academies: List<Academy>): DialogFragment() {
    private val binding by lazy{ DialogCreateClassroomBinding.inflate(layoutInflater)}

    private var selectedAcademy: Academy? = null

    private var previousName = ""

    private var isLayoutVisible = false
    private var isNameNotContainsSemicolon = true

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ManageClassroomViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())

        initialize()
        initViewModel()
        setSpinner()
        setListeners()

        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        _dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        _dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return _dialog
    }

    private fun initialize(){
        binding.dayPickerMakeClassroom.locale = Locale.KOREAN

        binding.textNewClassroomName.addTextChangedListener(watcher)
    }

    private fun initViewModel(){
        viewModel.createClassroomMessage.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"${it.data}",Toast.LENGTH_SHORT).show()
                    parentFragment?.setFragmentResult("createNewClassroom", bundleOf("isCreated" to "yes"))
                    dismiss()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setListeners(){
        binding.buttonMakeClassroom.setOnClickListener {
            makeClassroom()
        }

        binding.dayPickerMakeClassroom.setDaySelectionChangedListener {
            buttonEnabledIfAllSelected()
        }
    }

    private fun setSpinner(){
        val adapter: ArrayAdapter<Academy> = ArrayAdapter(requireContext(),R.layout.simple_spinner_dropdown_item,academies)
        binding.spinnerMakeClassroomSelectAcademy.adapter = adapter

        binding.spinnerMakeClassroomSelectAcademy.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAcademy = academies[position]
                if(!isLayoutVisible)isLayoutVisible = true
                changeLayoutVisibility()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                isLayoutVisible = false
                changeLayoutVisibility()
            }
        }
    }

    private fun changeLayoutVisibility(){
        if(isLayoutVisible){
            val anim = AnimationUtils.loadAnimation(requireContext(), com.ich.pullgo.R.anim.alpha)
            binding.layoutMakeClassroomExcludeAcademySelect.visibility = View.VISIBLE
            binding.layoutMakeClassroomExcludeAcademySelect.startAnimation(anim)
        }else{
            binding.layoutMakeClassroomExcludeAcademySelect.visibility = View.GONE
        }
    }

    private fun buttonEnabledIfAllSelected(){
        binding.buttonMakeClassroom.isEnabled = isAllSelected()
    }

    private fun isAllSelected(): Boolean{
        val academySelected = (selectedAcademy != null)
        val classroomNameInput = (binding.textNewClassroomName.text.toString() != "")
        val daySelected = (binding.dayPickerMakeClassroom.selectedDays.isNotEmpty())

        return academySelected && classroomNameInput && daySelected && isNameNotContainsSemicolon
    }

    private val watcher=object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            checkSemicolonInName(s)
            buttonEnabledIfAllSelected()
        }
    }

    private fun checkSemicolonInName(s: Editable?){
        if(s?.contains(';') == true){
            binding.textNewClassroomName.setText(previousName)
            binding.layoutNewClassroomName.error = ";는 제외하고 입력해주세요"
            binding.textNewClassroomName.setSelection(previousName.length)
        }else{
            binding.layoutNewClassroomName.error = null
            previousName = s.toString()
        }
    }

    private fun makeSelectedDaysToString(): String{
        val str = StringBuilder("")

        for(day in binding.dayPickerMakeClassroom.selectedDays){
            when(day.name){
                "SUNDAY" -> str.append("일")
                "MONDAY" -> str.append("월")
                "TUESDAY" -> str.append("화")
                "WEDNESDAY" -> str.append("수")
                "THURSDAY" -> str.append("목")
                "FRIDAY" -> str.append("금")
                "SATURDAY" -> str.append("토")
            }
        }
        return str.toString()
    }

    private fun makeClassroom(){
        val name = binding.textNewClassroomName.text.toString()
        val creatorId = app.loginUser.teacher?.id!!
        val days = makeSelectedDaysToString()
        val classroomName = "$name;$days"

        val newClassroom = Classroom(selectedAcademy?.id!!,classroomName,creatorId)

        viewModel.createClassroom(newClassroom)
    }

    companion object {
        const val TAG_LESSON_INFO_DIALOG = "make_classroom_dialog"
    }
}