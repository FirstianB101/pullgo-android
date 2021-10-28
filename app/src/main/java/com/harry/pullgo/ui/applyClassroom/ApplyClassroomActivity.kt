package com.harry.pullgo.ui.applyClassroom

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.ClassroomAdapter
import com.harry.pullgo.data.api.OnClassroomClickListener
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.repository.ApplyClassroomRepository
import com.harry.pullgo.databinding.ActivityRequestApplyClassroomBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApplyClassroomActivity : AppCompatActivity() {
    val binding by lazy{ActivityRequestApplyClassroomBinding.inflate(layoutInflater)}

    private val viewModel: ApplyClassroomViewModel by viewModels()

    private var selectedAcademy: Academy? = null
    private var selectedClassroom: Classroom? = null
    private var isLayoutVisible = false

    private val app: PullgoApplication by lazy{ application as PullgoApplication}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        initViewModel()
    }

    private fun initViewModel(){
        viewModel.appliedAcademiesRepository.observe(this){
            setSpinnerItems()
        }

        viewModel.applyClassroomsRepositories.observe(this){
            displayClassrooms()
            app.dismissLoadingDialog()
        }

        viewModel.appliedClassroomsMessage.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            app.dismissLoadingDialog()
        }

        if(app.loginUser.teacher != null) {
            viewModel.requestTeacherAppliedAcademies(app.loginUser.teacher?.id!!)
        }else if(app.loginUser.student != null){
            viewModel.requestStudentAppliedAcademies(app.loginUser.student?.id!!)
        }

        app.showLoadingDialog(supportFragmentManager)
    }

    private fun initialize(){
        binding.toolbarApplyClassroom.title = "반 가입 요청"
        binding.applyClassroomRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.buttonApplyClassroomSearch.setOnClickListener {
            val searchName = binding.applyClassroomSearchText.text.toString()
            viewModel.requestGetClassrooms(selectedAcademy?.id!!,searchName)
            app.showLoadingDialog(supportFragmentManager)
        }
    }

    private fun setSpinnerItems(){
        val academies = viewModel.appliedAcademiesRepository.value!!

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
        val data = viewModel.applyClassroomsRepositories.value

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
        dialog.start("${information?.get(0)} (${information?.get(2)})","${information?.get(1)} 선생님","가입 요청","취소")
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
        if(app.loginUser.student != null) {
            viewModel.requestStudentApplyClassroom(app.loginUser.student?.id!!, selectedClassroom)
        }else if(app.loginUser.teacher != null){
            viewModel.requestTeacherApplyClassroom(app.loginUser.teacher?.id!!, selectedClassroom)
        }
    }
}