package com.harry.pullgo.ui.manageQuestion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.harry.pullgo.R
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.databinding.FragmentManageQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentManageQuestion(
    private val question: Question
) : Fragment() {
    private val binding by lazy{FragmentManageQuestionBinding.inflate(layoutInflater)}

    private var curImageUri: String? = null

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        setListeners()

        return binding.root
    }

    private fun initialize(){
        binding.editTextManageQuestion.setText(question.content)
        curImageUri = question.pictureUrl

        Glide.with(this)
            .load(question.pictureUrl)
            .error(R.drawable.image_load_error)
            .into(binding.imageViewManageQuestion)

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                //curImageUri = it.data?.dataString
                curImageUri = "https://i.imgur.com/JOKsNeT.jpg"
                Glide.with(this)
                    .load(curImageUri)
                    .into(binding.imageViewManageQuestion)
            }
        }
    }

    private fun setListeners(){
        binding.imageViewManageQuestion.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startForResult.launch(intent)
        }
    }

    fun getCurrentContent() = binding.editTextManageQuestion.text.toString()
    fun getCurImageUrl() = curImageUri
}