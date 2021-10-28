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
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.LessonAdapter
import com.harry.pullgo.data.api.OnCalendarResetListener
import com.harry.pullgo.data.api.OnLessonClickListener
import com.harry.pullgo.data.models.Lesson
import com.harry.pullgo.data.repository.LessonsRepository
import com.harry.pullgo.databinding.FragmentCalendarBottomSheetBinding
import com.harry.pullgo.ui.studentFragment.FragmentLessonInfoDialog
import com.harry.pullgo.ui.teacherFragment.FragmentLessonInfoManageDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCalendarBottomSheet(private val selectedDate: String) : BottomSheetDialogFragment(){
    private val binding by lazy{FragmentCalendarBottomSheetBinding.inflate(layoutInflater)}

    private val viewModel: LessonsViewModel by viewModels()

    var calendarResetListenerListener: OnCalendarResetListener? = null

    private val app: PullgoApplication by lazy { requireActivity().application as PullgoApplication }

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
            app.dismissLoadingDialog()
        }

        if(app.loginUser.teacher != null){
            viewModel.requestTeacherLessonOnDate(app.loginUser.teacher?.id!!,selectedDate)
        }else if(app.loginUser.student != null){
            viewModel.requestStudentLessonOnDate(app.loginUser.student?.id!!,selectedDate)
        }

        app.showLoadingDialog(childFragmentManager)

        setFragmentResultListener("isLessonPatched"){ _, bundle ->
            if(bundle.getString("Patched") == "yes"){
                viewModel.requestTeacherLessonOnDate(app.loginUser.teacher?.id!!,selectedDate)
                app.showLoadingDialog(childFragmentManager)
            }
        }
    }

    private fun showLessons(){
        val lessons = viewModel.dayLessonsRepositories.value
        val adapter = LessonAdapter(lessons!!)

        if(app.loginUser?.student != null){ // student
            adapter.itemClickListener = object: OnLessonClickListener{
                override fun onLessonClick(view: View, lesson: Lesson?) {
                    FragmentLessonInfoDialog(lesson!!).show(childFragmentManager, FragmentLessonInfoDialog.TAG_LESSON_INFO_DIALOG)
                }
            }
        }else if(app.loginUser?.teacher != null){ // teacher
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