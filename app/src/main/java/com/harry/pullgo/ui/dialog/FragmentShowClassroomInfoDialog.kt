package com.harry.pullgo.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.databinding.DialogAcademyInfoBinding
import com.harry.pullgo.databinding.DialogClassroomInfoBinding
import com.harry.pullgo.databinding.DialogManageClassroomStudentInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentShowClassroomInfoDialog(private val selectedClassroom: Classroom): DialogFragment() {
    private val binding by lazy{DialogClassroomInfoBinding.inflate(layoutInflater)}

    @Inject
    lateinit var service: PullgoService

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        initialize()
        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        _dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return _dialog
    }

    private fun initialize(){
        setAcademyInfo()
        val classroomInfo = selectedClassroom.name?.split(';')
        binding.textViewClassroomInfoClassroomName.text = classroomInfo?.get(0)
        binding.textViewClassroomInfoClassroomCreator.text = selectedClassroom.creator?.account?.fullName ?: ""
        binding.buttonClassroomInfo.setOnClickListener { dismiss() }
    }

    private fun setAcademyInfo(){
        CoroutineScope(Dispatchers.IO).launch {
            service.getOneAcademy(selectedClassroom.academyId!!).let{
                if(it.isSuccessful){
                    binding.textViewClassroomInfoClassroomAcademy.text = it.body()?.name
                }
            }
        }
    }

    companion object {
        const val TAG_CLASSROOM_INFO_DIALOG = "classroom_info_dialog"
    }
}