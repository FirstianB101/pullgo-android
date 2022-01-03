package com.ich.pullgo.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/dialog/FragmentShowClassroomInfoDialog.kt
import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.data.models.Classroom
import com.ich.pullgo.databinding.DialogClassroomInfoBinding
import com.ich.pullgo.di.PullgoRetrofitService
=======
import com.ich.pullgo.data.remote.PullgoService
import com.ich.pullgo.databinding.DialogClassroomInfoBinding
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Classroom
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/dialog/FragmentShowClassroomInfoDialog.kt
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentShowClassroomInfoDialog(private val selectedClassroom: Classroom): DialogFragment() {
    private val binding by lazy{DialogClassroomInfoBinding.inflate(layoutInflater)}

    @Inject
    @PullgoRetrofitService
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