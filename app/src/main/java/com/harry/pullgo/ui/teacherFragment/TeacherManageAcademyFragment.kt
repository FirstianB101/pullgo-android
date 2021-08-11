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
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.ManageAcademyRepository
import com.harry.pullgo.databinding.FragmentTeacherManageAcademyBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import com.harry.pullgo.ui.manageAcademy.FragmentManageAcademyDelegateDialog
import com.harry.pullgo.ui.manageAcademy.ManageAcademyManagePeopleActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherManageAcademyFragment: Fragment() {
    private val binding by lazy{FragmentTeacherManageAcademyBinding.inflate(layoutInflater)}
    private val client by lazy{RetrofitClient.getApiService()}

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
    
    private fun showDeleteAcademyDialog(){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                deleteAcademy()
            }
        }
        dialog.start("학원 삭제","${selectedAcademy.name} 학원을 삭제하시겠습니까?","삭제하기","취소")
    }

    private fun deleteAcademy(){
        client.deleteAcademy(selectedAcademy.id!!).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Snackbar.make(binding.root,"학원이 삭제되었습니다",Snackbar.LENGTH_SHORT).show()
                    viewModel.requestGetOwnedAcademies(LoginInfo.loginTeacher?.id!!)
                    makeLayoutInvisible()
                }else{
                    Toast.makeText(requireContext(),"학원을 삭제하지 못했습니다",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(requireContext(),"서버와 연결에 실패했습니다",Toast.LENGTH_SHORT).show()
            }
        })
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

        client.editAcademy(selectedAcademy.id!!,selectedAcademy).enqueue(object: Callback<Academy>{
            override fun onResponse(call: Call<Academy>, response: Response<Academy>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),"수정되었습니다",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),"수정하지 못했습니다",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Academy>, t: Throwable) {
                Toast.makeText(requireContext(),"서버와 연결에 실패했습니다",Toast.LENGTH_SHORT).show()
            }
        })
    }
}