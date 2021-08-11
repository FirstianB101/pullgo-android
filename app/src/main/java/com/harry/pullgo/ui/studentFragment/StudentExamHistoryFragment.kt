package com.harry.pullgo.ui.studentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.R
import com.harry.pullgo.databinding.FragmentStudentExamHistoryBinding

class StudentExamHistoryFragment : Fragment(), AdapterView.OnItemSelectedListener{
    private val binding by lazy{ FragmentStudentExamHistoryBinding.inflate(layoutInflater)}
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ArrayAdapter.createFromResource(requireContext(), R.array.exam_history_filter,android.R.layout.simple_spinner_item)
            .also { adapter->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerExamHistory.adapter=adapter
            }
        binding.spinnerExamHistory.prompt="정렬"
        binding.spinnerExamHistory.onItemSelectedListener=this
        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Snackbar.make(binding.root,"$position 선택", Snackbar.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}