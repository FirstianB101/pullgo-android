package com.harry.pullgo.ui.dialog

import android.R
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.objects.MakeClassroom
import com.harry.pullgo.databinding.DialogMakeClassroomBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FragmentMakeClassroomDialog(private val academies: List<Academy>): DialogFragment() {
    private val binding by lazy{DialogMakeClassroomBinding.inflate(layoutInflater)}

    private var selectedAcademy: Academy? = null

    private var previousName = ""

    private var isLayoutVisible = false
    private var isNameNotContainsSemicolon = true

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())

        initialize()
        setSpinner()
        setListeners()

        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        _dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return _dialog
    }

    private fun initialize(){
        binding.dayPickerMakeClassroom.locale = Locale.KOREAN

        binding.textNewClassroomName.addTextChangedListener(watcher)
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
            val anim = AnimationUtils.loadAnimation(requireContext(), com.harry.pullgo.R.anim.alpha)
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
        val client = RetrofitClient.getApiService()

        val name = binding.textNewClassroomName.text.toString()
        val teacher = LoginInfo.loginTeacher?.account?.fullName
        val days = makeSelectedDaysToString()
        val classroomName = "$name;$teacher;$days"

        val newClassroom = MakeClassroom(classroomName,selectedAcademy?.id!!,LoginInfo.loginTeacher?.id!!)

        client.createClassroom(newClassroom).enqueue(object:
            Callback<Classroom> {
            override fun onResponse(call: Call<Classroom>, response: Response<Classroom>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),"반이 생성되었습니다",Toast.LENGTH_SHORT).show()
                    parentFragment?.setFragmentResult("createNewClassroom", bundleOf("isCreated" to "yes"))
                    dismiss()
                }else{
                    Toast.makeText(requireContext(),"반을 생성하지 못했습니다",Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }

            override fun onFailure(call: Call<Classroom>, t: Throwable) {
                Toast.makeText(requireContext(),"서버와 연결에 실패했습니다",Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        const val TAG_LESSON_INFO_DIALOG = "make_classroom_dialog"
    }
}