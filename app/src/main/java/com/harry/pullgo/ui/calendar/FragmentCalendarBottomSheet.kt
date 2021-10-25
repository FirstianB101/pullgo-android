package com.harry.pullgo.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harry.pullgo.R
import com.harry.pullgo.data.adapter.LessonAdapter
import com.harry.pullgo.data.api.OnCalendarResetListener
import com.harry.pullgo.data.api.OnLessonClickListener
import com.harry.pullgo.data.models.Lesson
import com.harry.pullgo.data.objects.LoadingDialog
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.LessonsRepository
import com.harry.pullgo.databinding.FragmentCalendarBottomSheetBinding
import com.harry.pullgo.ui.studentFragment.FragmentLessonInfoDialog
import com.harry.pullgo.ui.teacherFragment.FragmentLessonInfoManageDialog

class FragmentCalendarBottomSheet(private val selectedDate: String) : BottomSheetDialogFragment(){
    private val binding by lazy{FragmentCalendarBottomSheetBinding.inflate(layoutInflater)}

    private val viewModel: LessonsViewModel by viewModels{LessonsViewModelFactory(LessonsRepository(requireContext()))}

    var calendarResetListenerListener: OnCalendarResetListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize()
        setViewModel()

        return binding.root
    }

    private fun initialize(){
        dialog!!.setCanceledOnTouchOutside(true)

        binding.textViewShowDate.text = selectedDate

        binding.recyclerViewBottomSheet.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setViewModel(){
        viewModel.dayLessonsRepositories.observe(requireActivity()){
            showLessons()
            LoadingDialog.dialog.dismiss()
        }

        if(LoginInfo.user?.teacher != null){
            viewModel.requestTeacherLessonOnDate(LoginInfo.user?.teacher?.id!!,selectedDate)
        }else if(LoginInfo.user?.student != null){
            viewModel.requestStudentLessonOnDate(LoginInfo.user?.student?.id!!,selectedDate)
        }

        LoadingDialog.dialog.show(childFragmentManager, LoadingDialog.loadingDialogStr)

        setFragmentResultListener("isLessonPatched"){ _, bundle ->
            if(bundle.getString("Patched") == "yes"){
                viewModel.requestTeacherLessonOnDate(LoginInfo.user?.teacher?.id!!,selectedDate)
                LoadingDialog.dialog.show(childFragmentManager, LoadingDialog.loadingDialogStr)
            }
        }
    }

    private fun showLessons(){
        val lessons = viewModel.dayLessonsRepositories.value
        val adapter = LessonAdapter(lessons!!)

        if(LoginInfo.user?.student != null){ // student
            adapter.itemClickListener = object: OnLessonClickListener{
                override fun onLessonClick(view: View, lesson: Lesson?) {
                    FragmentLessonInfoDialog(lesson!!).show(childFragmentManager, FragmentLessonInfoDialog.TAG_LESSON_INFO_DIALOG)
                }
            }
        }else if(LoginInfo.user?.teacher != null){ // teacher
            adapter.itemClickListener = object: OnLessonClickListener{
                override fun onLessonClick(view: View, lesson: Lesson?) {
                    val dialog = FragmentLessonInfoManageDialog(lesson!!)
                    dialog.calendarResetListenerListener = calendarResetListenerListener
                    dialog.show(childFragmentManager, FragmentLessonInfoManageDialog.TAG_LESSON_INFO_MANAGE_DIALOG)
                }
            }
        }

        binding.recyclerViewBottomSheet.adapter = adapter

        val lessonNumText = if(lessons.isNotEmpty()) "${lessons.size}개의 수업이 있습니다"
                            else "해당 날짜에 수업이 없습니다"
        binding.textViewLessonNum.text = lessonNumText
    }
}