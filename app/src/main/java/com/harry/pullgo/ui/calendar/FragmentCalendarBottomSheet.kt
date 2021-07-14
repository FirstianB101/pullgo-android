package com.harry.pullgo.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harry.pullgo.data.api.OnLessonClick
import com.harry.pullgo.data.objects.Lesson
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.databinding.FragmentCalendarBottomSheetBinding
import com.harry.pullgo.ui.FragmentLessonInfoDialog
import com.harry.pullgo.ui.FragmentLessonInfoManageDialog

class FragmentCalendarBottomSheet : BottomSheetDialogFragment(){
    private val binding by lazy{FragmentCalendarBottomSheetBinding.inflate(layoutInflater)}

    var date: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initialize()

        return binding.root
    }

    private fun initialize(){
        dialog!!.setCanceledOnTouchOutside(true)
        val dateBundle = arguments
        if (dateBundle != null) {
            date = dateBundle.getString("date")
            binding.textViewShowDate.text = date
        }

        setItemClickListenerByAuthor()
        binding.recyclerViewBottomSheet.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setItemClickListenerByAuthor(){
        val testLessons = Array(2){
            Lesson("Lesson for test",null,null)
        }
        val adapter = BottomSheetLessonsAdapter(testLessons)

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
    }
}