package com.ich.pullgo.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.adapter.LessonAdapter
import com.ich.pullgo.data.api.OnCalendarResetListener
import com.ich.pullgo.data.api.OnLessonClickListener
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/calendar/FragmentCalendarBottomSheet.kt
import com.ich.pullgo.data.models.Lesson
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentCalendarBottomSheetBinding
=======
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentCalendarBottomSheetBinding
import com.ich.pullgo.domain.model.Lesson
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/calendar/FragmentCalendarBottomSheet.kt
import com.ich.pullgo.ui.studentFragment.FragmentLessonInfoDialog
import com.ich.pullgo.ui.teacherFragment.FragmentLessonInfoManageDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentCalendarBottomSheet(private val selectedDate: String) : BottomSheetDialogFragment(){
    private val binding by lazy{FragmentCalendarBottomSheetBinding.inflate(layoutInflater)}

    private val viewModel: LessonsViewModel by viewModels()

    var calendarResetListener: OnCalendarResetListener? = null

    @Inject
    lateinit var app: PullgoApplication

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
            when(it.status){
                Status.LOADING -> {
                    app.showLoadingDialog(childFragmentManager)
                }
                Status.SUCCESS -> {
                    showLessons()
                    app.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    app.dismissLoadingDialog()
                    Toast.makeText(requireContext(),"수업 정보를 불러올 수 없습니다(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        if(app.loginUser.teacher != null){
            viewModel.requestTeacherLessonOnDate(app.loginUser.teacher?.id!!,selectedDate)
        }else if(app.loginUser.student != null){
            viewModel.requestStudentLessonOnDate(app.loginUser.student?.id!!,selectedDate)
        }

        setFragmentResultListener("isLessonPatched"){ _, bundle ->
            if(bundle.getString("Patched") == "yes"){
                viewModel.requestTeacherLessonOnDate(app.loginUser.teacher?.id!!,selectedDate)
            }
        }
    }

    private fun showLessons(){
        val lessons = viewModel.dayLessonsRepositories.value?.data
        val adapter = LessonAdapter(lessons!!)

        if(app.loginUser.student != null){ // student
            adapter.itemClickListener = object: OnLessonClickListener{
                override fun onLessonClick(view: View, lesson: Lesson?) {
                    FragmentLessonInfoDialog(lesson!!).show(childFragmentManager, FragmentLessonInfoDialog.TAG_LESSON_INFO_DIALOG)
                }
            }
        }else if(app.loginUser.teacher != null){ // teacher
            adapter.itemClickListener = object: OnLessonClickListener{
                override fun onLessonClick(view: View, lesson: Lesson?) {
                    val dialog = FragmentLessonInfoManageDialog(lesson!!)
                    dialog.calendarResetListener = calendarResetListener
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