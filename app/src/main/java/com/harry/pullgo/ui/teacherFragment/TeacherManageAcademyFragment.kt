package com.harry.pullgo.ui.teacherFragment

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
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.ManageAcademyRepository
import com.harry.pullgo.databinding.FragmentTeacherManageAcademyBinding
import com.harry.pullgo.ui.manageAcademy.FragmentManageAcademyDelegateDialog
import com.harry.pullgo.ui.manageAcademy.ManageAcademyManagePeopleActivity

class TeacherManageAcademyFragment: Fragment() {
    private val binding by lazy{FragmentTeacherManageAcademyBinding.inflate(layoutInflater)}

    private lateinit var viewModel: TeacherManageAcademyViewModel

    private var isLayoutVisible = false
    private var isEditMode = false

    private lateinit var selectedAcademy: Academy

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize()
        initViewModel()
        setListeners()

        return binding.root
    }

    private fun initialize(){

    }

    private fun initViewModel(){
        val factory = TeacherManageAcademyViewModelFactory(ManageAcademyRepository())
        viewModel = ViewModelProvider(requireActivity(),factory).get(TeacherManageAcademyViewModel::class.java)

        viewModel.ownedAcademiesRepository.observe(requireActivity()){
            setSpinnerItems()
        }

        viewModel.requestGetOwnedAcademies(LoginInfo.loginTeacher?.id!!)
    }

    private fun setListeners(){
        binding.buttonManageAcademyEdit.setOnClickListener {
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

        }
    }

    private fun setSpinnerItems(){
        val academies = viewModel.ownedAcademiesRepository.value!!

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

        val anim = AnimationUtils.loadAnimation(requireContext(), com.harry.pullgo.R.anim.alpha)
        binding.layoutManageAcademy.startAnimation(anim)

        isLayoutVisible = true
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
}