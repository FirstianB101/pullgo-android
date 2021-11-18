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
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.databinding.DialogAcademyInfoBinding
import com.harry.pullgo.databinding.DialogManageClassroomStudentInfoBinding
import com.harry.pullgo.di.PullgoRetrofitService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentShowAcademyInfoDialog(private val selectedAcademy: Academy): DialogFragment() {
    private val binding by lazy{DialogAcademyInfoBinding.inflate(layoutInflater)}

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
        setOwner()
        binding.textViewAcademyInfoAcademyName.text = selectedAcademy.name
        binding.textViewAcademyInfoAcademyAddress.text = selectedAcademy.address
        binding.textViewAcademyInfoAcademyPhone.text = selectedAcademy.phone
        binding.buttonAcademyInfo.setOnClickListener { dismiss() }
    }

    private fun setOwner(){
        CoroutineScope(Dispatchers.IO).launch {
            service.getOneTeacher(selectedAcademy.ownerId!!).let{
                if(it.isSuccessful){
                    binding.textViewAcademyInfoAcademyOwner.text = it.body()?.account?.fullName ?: ""
                }
            }
        }
    }

    companion object {
        const val TAG_ACADEMY_INFO_DIALOG = "academy_info_dialog"
    }
}