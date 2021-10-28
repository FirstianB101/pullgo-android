package com.harry.pullgo.ui.teacherFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.harry.pullgo.ui.findAcademy.FindAcademyActivity
import com.harry.pullgo.databinding.FragmentTeacherHomeNoAcademyBinding
import com.harry.pullgo.ui.findAcademy.CreateAcademyActivity
import com.harry.pullgo.ui.main.TeacherMainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherHomeFragmentNoAcademy: Fragment() {
    private val binding by lazy{FragmentTeacherHomeNoAcademyBinding.inflate(layoutInflater)}

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        initialize()
        setListeners()

        return binding.root
    }

    private fun initialize(){
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                if(it.data?.getStringExtra("createAcademy") == "yes"){
                    val mainIntent = Intent(requireContext(), TeacherMainActivity::class.java)
                    mainIntent.putExtra("appliedAcademyExist",true)
                    mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(mainIntent)
                }
            }
        }
    }

    private fun setListeners(){
        binding.buttonTeacherSignUpAcademy.setOnClickListener {
            val intent= Intent(requireContext(), FindAcademyActivity::class.java)
            startActivity(intent)
        }

        binding.buttonTeacherCreateAcademy.setOnClickListener {
            val intent = Intent(requireContext(), CreateAcademyActivity::class.java)

            startForResult.launch(intent)
        }
    }
}