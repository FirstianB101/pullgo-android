package com.ich.pullgo.ui.manageClassroom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.ich.pullgo.data.models.Classroom
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentManageClasssroomEditClassroomBinding
import com.ich.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditClassroomFragment(private val selectedClassroom: Classroom): Fragment() {
    private val binding by lazy{FragmentManageClasssroomEditClassroomBinding.inflate(layoutInflater)}

    private var isEditMode = false
    private var isFormatGood = true

    private val viewModel: ManageClassroomViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        setListeners()
        initViewModel()

        return binding.root
    }

    private fun initialize(){
        val info = selectedClassroom.name?.split(';')
        val creator = selectedClassroom.creator

        binding.textEditClassroomName.setText(info?.get(0))
        binding.textEditClassroomTeacherName.setText(creator?.account?.fullName)
        binding.textEditClassroomDate.setText(info?.get(1))

        binding.textEditClassroomName.addTextChangedListener(watcher)
        binding.textEditClassroomDate.addTextChangedListener(watcher)
        binding.textEditClassroomTeacherName.isFocusable = false
        binding.textEditClassroomTeacherName.isClickable = false
    }

    private fun setListeners(){
        binding.buttonEditClassroomEdit.setOnClickListener {
            if(isEditMode){
                if(isFormatGood) {
                    setLayoutsDisabled()
                    requestEditClassroom()
                    binding.buttonEditClassroomEdit.text = "수정하기"
                    isEditMode = false
                }else{
                    Snackbar.make(binding.root,"사용할 수 없는 문자가 포함되어 있습니다",Snackbar.LENGTH_SHORT).show()
                }
            }else{
                setLayoutsEnabled()
                binding.textEditClassroomName.requestFocus()
                binding.buttonEditClassroomEdit.text = "수정 완료"
                isEditMode = true
            }
        }

        binding.buttonEditClassroomRemove.setOnClickListener {
            makeRemovePopup()
        }
    }

    private fun initViewModel(){
        viewModel.editClassroomMessage.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"${it.data}",Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.putExtra("finishedFragment","editClassroom")
                    requireActivity().setResult(Activity.RESULT_OK,intent)
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.deleteClassroomMessage.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"${it.data}",Toast.LENGTH_SHORT).show()
                    finishActivity()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setLayoutsEnabled(){
        binding.textEditClassroomName.isEnabled = true
        binding.textEditClassroomDate.isEnabled = true
        binding.textEditClassroomTeacherName.isEnabled = true

        Snackbar.make(binding.root,"편집 모드가 활성화 되었습니다",Snackbar.LENGTH_SHORT).show()
    }

    private fun setLayoutsDisabled(){
        binding.textEditClassroomName.isEnabled = false
        binding.textEditClassroomDate.isEnabled = false
        binding.textEditClassroomTeacherName.isEnabled = false
    }

    private fun requestEditClassroom(){
        val name = binding.textEditClassroomName.text.toString()
        val date = binding.textEditClassroomDate.text.toString()
        
        selectedClassroom.name = "$name;$date"

        viewModel.editClassroom(selectedClassroom.id!!,selectedClassroom)
    }

    private fun makeRemovePopup(){
        val info = selectedClassroom.name?.split(';')
        val creatorName = selectedClassroom.creator?.account?.fullName

        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                viewModel.deleteClassroom(selectedClassroom.id!!)
            }
        }

        dialog.start("다음 수업을 삭제하시겠습니까?","${info?.get(0)} - $creatorName - ${info?.get(1)}","확인","취소")
    }

    private fun finishActivity(){
        val intent = Intent()
        intent.putExtra("finishedFragment","editClassroom")
        requireActivity().setResult(Activity.RESULT_OK,intent)
        requireActivity().finish()
    }

    private val watcher=object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            setErrorMessages()
        }
    }

    private fun setErrorMessages(){
        isFormatGood = true

        if(binding.textEditClassroomName.text?.contains(';') == true) {
            binding.layoutEditClassroomName.error = ";문자는 사용할 수 없습니다"
            isFormatGood = false
        }else
            binding.layoutEditClassroomName.error = null


        if(binding.textEditClassroomDate.text?.contains(';') == true) {
            binding.layoutEditClassroomDate.error = ";문자는 사용할 수 없습니다"
            isFormatGood = false
        }else
            binding.layoutEditClassroomDate.error = null

    }
}