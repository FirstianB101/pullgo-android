package com.ich.pullgo.ui.teacherFragment

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.models.Academy
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentTeacherManageAcademyBinding
import com.ich.pullgo.ui.dialog.TwoButtonDialog
import com.ich.pullgo.ui.main.TeacherMainActivity
import com.ich.pullgo.ui.manageAcademy.FragmentManageAcademyDelegateDialog
import com.ich.pullgo.ui.manageAcademy.ManageAcademyManagePeopleActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TeacherManageAcademyFragment: Fragment() {
    private val binding by lazy{FragmentTeacherManageAcademyBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: TeacherManageAcademyViewModel by activityViewModels()

    private var isLayoutVisible = false
    private var isEditMode = false

    private lateinit var selectedAcademy: Academy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize()
        setListeners()

        return binding.root
    }

    private fun initialize(){
        makeLayoutInvisible()
    }

    override fun onStart() {
        super.onStart()
        viewModel.requestGetOwnedAcademies(app.loginUser.teacher?.id!!)
    }

    private fun initViewModel(){
        viewModel.ownedAcademiesRepository.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    setSpinnerItems()

                    if(it.data?.isEmpty() == true)
                        resetActivityWhenDeleteLastAcademy()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"학원 정보를 불러올 수 없습니다(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.manageAcademyMessage.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"${it.data}",Toast.LENGTH_SHORT).show()
                    if(it.data == "학원이 삭제되었습니다") {
                        viewModel.requestGetOwnedAcademies(app.loginUser.teacher?.id!!)
                        makeLayoutInvisible()
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setListeners(){
        binding.buttonManageAcademyEdit.setOnClickListener {
            if(isEditMode)editAcademy()
            editModeOn()
        }

        binding.buttonManageAcademyManagePeople.setOnClickListener {
            val intent = Intent(requireContext(),ManageAcademyManagePeopleActivity::class.java)
            intent.putExtra("selectedAcademyId",selectedAcademy.id!!)
            intent.putExtra("selectedAcademyName",selectedAcademy.name!!)
            startActivity(intent)
        }

        binding.buttonManageAcademyDelegate.setOnClickListener {
            FragmentManageAcademyDelegateDialog(selectedAcademy)
                .show(childFragmentManager,FragmentManageAcademyDelegateDialog.TAG_MANAGE_ACADEMY_DELEGATE_DIALOG)
        }

        binding.buttonManageAcademyDelete.setOnClickListener {
            showDeleteAcademyDialog()
        }
    }

    private fun resetActivityWhenDeleteLastAcademy(){
        val mainIntent = Intent(requireContext(), TeacherMainActivity::class.java)
        mainIntent.putExtra("appliedAcademyExist",false)
        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(mainIntent)
    }
    
    private fun showDeleteAcademyDialog(){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                viewModel.deleteAcademy(selectedAcademy.id!!)
            }
        }
        dialog.start("학원 삭제","${selectedAcademy.name} 학원을 삭제하시겠습니까?","삭제하기","취소")
    }

    private fun setSpinnerItems(){
        val academies = viewModel.ownedAcademiesRepository.value?.data!!

        val adapter: ArrayAdapter<Academy> = ArrayAdapter(requireContext(),R.layout.simple_spinner_dropdown_item,academies)
        binding.spinnerManageAcademySelect.adapter = adapter

        binding.spinnerManageAcademySelect.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAcademy = academies[position]
                makeLayoutVisible()
                fillInformation()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun makeLayoutVisible(){
        if(isLayoutVisible)return

        binding.layoutManageAcademy.visibility = View.VISIBLE

        val anim = AnimationUtils.loadAnimation(requireContext(), com.ich.pullgo.R.anim.alpha)
        binding.layoutManageAcademy.startAnimation(anim)

        isLayoutVisible = true
    }

    private fun makeLayoutInvisible(){
        if(!isLayoutVisible)return

        binding.layoutManageAcademy.visibility = View.GONE

        isLayoutVisible = false
    }

    private fun fillInformation(){
        binding.textManageAcademyAddress.setText(selectedAcademy.address.toString())
        binding.textManageAcademyPhone.setText(selectedAcademy.phone.toString())
    }

    private fun editModeOn(){
        if(isEditMode){
            binding.textManageAcademyAddress.isEnabled = false
            binding.textManageAcademyPhone.isEnabled = false

            binding.buttonManageAcademyEdit.text = "수정하기"

            isEditMode = false
        }else{
            binding.textManageAcademyAddress.isEnabled = true
            binding.textManageAcademyPhone.isEnabled = true

            binding.buttonManageAcademyEdit.text = "수정완료"

            binding.textManageAcademyAddress.requestFocus()

            isEditMode = true
        }
    }

    private fun editAcademy(){
        selectedAcademy.phone = binding.textManageAcademyPhone.text.toString()
        selectedAcademy.address = binding.textManageAcademyAddress.text.toString()

        viewModel.editAcademy(selectedAcademy.id!!,selectedAcademy)
    }
}