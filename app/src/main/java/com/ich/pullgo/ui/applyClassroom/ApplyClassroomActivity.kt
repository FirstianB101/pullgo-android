package com.ich.pullgo.ui.applyClassroom

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.adapter.ClassroomAdapter
import com.ich.pullgo.data.api.OnClassroomClickListener
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.ActivityRequestApplyClassroomBinding
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ApplyClassroomActivity : AppCompatActivity() {
    private val binding by lazy{ActivityRequestApplyClassroomBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ApplyClassroomViewModel by viewModels()

    private var selectedAcademy: Academy? = null
    private var selectedClassroom: Classroom? = null
    private var isLayoutVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        initViewModel()
    }

    private fun initViewModel(){
        viewModel.appliedAcademiesRepository.observe(this){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    setSpinnerItems()
                }
                Status.ERROR -> {
                    binding.spinnerApplyClassroomSelectAcademy.errorText = "학원을 불러오지 못했습니다"
                }
            }
        }

        viewModel.applyClassroomsRepositories.observe(this){
            when(it.status){
                Status.LOADING -> {
                    app.showLoadingDialog(supportFragmentManager)
                }
                Status.SUCCESS -> {
                    displayClassrooms()
                    app.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    Toast.makeText(this,"반 정보를 불러오지 못했습니다(${it.message})",Toast.LENGTH_SHORT).show()
                    app.dismissLoadingDialog()
                }
            }
        }

        viewModel.appliedClassroomsMessage.observe(this){
            when(it.status){
                Status.LOADING -> {
                    app.showLoadingDialog(supportFragmentManager)
                }
                Status.SUCCESS -> {
                    app.dismissLoadingDialog()
                    Toast.makeText(this,"${it.data}",Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    app.dismissLoadingDialog()
                    Toast.makeText(this,"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        if(app.loginUser?.teacher != null) {
            viewModel.requestTeacherAppliedAcademies(app.loginUser?.teacher?.id!!)
        }else if(app.loginUser?.student != null){
            viewModel.requestStudentAppliedAcademies(app.loginUser?.student?.id!!)
        }
    }

    private fun initialize(){
        binding.toolbarApplyClassroom.title = "반 가입 요청"
        binding.applyClassroomRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.buttonApplyClassroomSearch.setOnClickListener {
            val searchName = binding.applyClassroomSearchText.text.toString()
            viewModel.requestGetClassrooms(selectedAcademy?.id!!,searchName)
        }
    }

    private fun setSpinnerItems(){
        val academies = viewModel.appliedAcademiesRepository.value?.data!!

        val adapter: ArrayAdapter<Academy> = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,academies)
        binding.spinnerApplyClassroomSelectAcademy.adapter = adapter

        if(academies.isEmpty())
            binding.textViewApplyClassroomNoAcademy.visibility = View.VISIBLE

        binding.spinnerApplyClassroomSelectAcademy.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAcademy = academies[position]
                resetSearchResults()
                makeLayoutsVisible()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun makeLayoutsVisible(){
        if(isLayoutVisible)return

        binding.spinnerApplyClassroomSelectAcademy.visibility = View.VISIBLE
        binding.searchAreaApplyClassroom.visibility = View.VISIBLE
        binding.applyClassroomRecyclerView.visibility = View.VISIBLE

        val anim = AnimationUtils.loadAnimation(this, R.anim.alpha)
        binding.spinnerApplyClassroomSelectAcademy.startAnimation(anim)
        binding.searchAreaApplyClassroom.startAnimation(anim)

        isLayoutVisible = true
    }

    private fun displayClassrooms(){
        val data = viewModel.applyClassroomsRepositories.value?.data

        val academyAdapter = data?.let {
            ClassroomAdapter(it)
        }

        if (academyAdapter != null) {
            academyAdapter.itemClickListener = object: OnClassroomClickListener {
                override fun onClassroomClick(view: View, classroom: Classroom?) {
                    selectedClassroom = classroom
                    showApplyRequestDialog(classroom)
                }
            }
        }

        changeVisibilityRecyclerAndTextview(data?.isEmpty() == true)

        binding.applyClassroomRecyclerView.adapter = academyAdapter
    }

    private fun showApplyRequestDialog(classroom: Classroom?){
        val dialog = TwoButtonDialog(this)
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                requestClassroomApply()
            }
        }
        val information = classroom?.name?.split(';')
        val creator = classroom?.creator
        dialog.start("${information?.get(0)} (${information?.get(1)})","${creator?.account?.fullName} 선생님","가입 요청","취소")
    }

    private fun changeVisibilityRecyclerAndTextview(isEmpty: Boolean){
        if(isEmpty){
            binding.applyClassroomRecyclerView.visibility = View.GONE
            binding.textViewApplyClassroomNoClassroom.visibility = View.VISIBLE
        }else{
            binding.applyClassroomRecyclerView.visibility = View.VISIBLE
            binding.textViewApplyClassroomNoClassroom.visibility = View.GONE
        }
    }

    private fun resetSearchResults(){
        viewModel.resetClassroomSearchResult()
    }

    fun requestClassroomApply(){
        if(app.loginUser?.student != null) {
            viewModel.requestStudentApplyClassroom(app.loginUser?.student?.id!!, selectedClassroom)
        }else if(app.loginUser?.teacher != null){
            viewModel.requestTeacherApplyClassroom(app.loginUser?.teacher?.id!!, selectedClassroom)
        }
    }
}