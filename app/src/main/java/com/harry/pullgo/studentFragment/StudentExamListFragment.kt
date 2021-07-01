package com.harry.pullgo.studentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.R
import com.harry.pullgo.databinding.FragmentStudentExamListBinding

class StudentExamListFragment : Fragment(), AdapterView.OnItemSelectedListener{
    private val binding by lazy{FragmentStudentExamListBinding.inflate(layoutInflater)}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ArrayAdapter.createFromResource(requireContext(),R.array.exam_list_filter,android.R.layout.simple_spinner_item)
            .also { adapter->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerExamList.adapter=adapter
            }
        binding.spinnerExamList.prompt="정렬"
        binding.spinnerExamList.onItemSelectedListener=this
        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Snackbar.make(binding.root,"$position 선택",Snackbar.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}