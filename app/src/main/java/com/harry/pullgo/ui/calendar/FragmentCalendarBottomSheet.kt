package com.harry.pullgo.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harry.pullgo.data.adapter.LessonAdapter
import com.harry.pullgo.data.api.OnLessonClick
import com.harry.pullgo.data.objects.Lesson
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.LessonsRepository
import com.harry.pullgo.databinding.FragmentCalendarBottomSheetBinding
import com.harry.pullgo.ui.dialog.FragmentLessonInfoDialog
import com.harry.pullgo.ui.dialog.FragmentLessonInfoManageDialog

class FragmentCalendarBottomSheet : BottomSheetDialogFragment(){
    private val binding by lazy{FragmentCalendarBottomSheetBinding.inflate(layoutInflater)}

    private lateinit var viewModel: LessonsViewModel
    private var date: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize()
        setViewModel()

        return binding.root
    }

    private fun initialize(){
        dialog!!.setCanceledOnTouchOutside(true)
        val dateBundle = arguments
        if (dateBundle != null) {
            date = dateBundle.getString("date")
            binding.textViewShowDate.text = date
        }

        binding.recyclerViewBottomSheet.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setViewModel(){
        viewModel = LessonsViewModel(LessonsRepository())

        viewModel.dayLessonsRepositories.observe(requireActivity()){
            showLessons()
        }

        if(LoginInfo.loginTeacher != null){
            viewModel.requestTeacherLessonOnDate(LoginInfo.loginTeacher?.id!!,date!!)
        }else if(LoginInfo.loginStudent != null){
            viewModel.requestStudentLessonOnDate(LoginInfo.loginStudent?.id!!,date!!)
        }
    }

    private fun showLessons(){
        val lessons = viewModel.dayLessonsRepositories.value
        val adapter = LessonAdapter(lessons)

        if(LoginInfo.loginStudent != null){ // student
            adapter.itemClickListener = object: OnLessonClick{
                override fun onLessonClick(view: View, lesson: Lesson?) {
                    FragmentLessonInfoDialog().show(childFragmentManager, FragmentLessonInfoDialog.TAG_LESSON_INFO_DIALOG)
                }
            }
        }else if(LoginInfo.loginTeacher != null){ // teacher
            adapter.itemClickListener = object: OnLessonClick{
                override fun onLessonClick(view: View, lesson: Lesson?) {
                    FragmentLessonInfoManageDialog().show(childFragmentManager, FragmentLessonInfoManageDialog.TAG_LESSON_INFO_MANAGE_DIALOG)
                }
            }
        }

        binding.recyclerViewBottomSheet.adapter = adapter
        val lessonNumText = if(lessons != null && lessons.isNotEmpty()) "${lessons?.size}개의 수업이 있습니다"
                            else "해당 날짜에 수업이 없습니다"
        binding.textViewLessonNum.text = lessonNumText
    }
}