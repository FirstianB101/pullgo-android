package com.harry.pullgo.ui.manageQuestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harry.pullgo.databinding.FragmentNoQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentNoQuestion: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentNoQuestionBinding.inflate(layoutInflater).root
    }
}