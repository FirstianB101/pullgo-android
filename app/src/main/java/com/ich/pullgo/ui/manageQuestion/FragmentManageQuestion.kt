package com.ich.pullgo.ui.manageQuestion

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.databinding.FragmentManageQuestionBinding
import com.ich.pullgo.domain.model.Question
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentManageQuestion(
    private val question: Question
) : Fragment() {
    private val binding by lazy{FragmentManageQuestionBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private var curImageUri: Uri? = null
    var isImageChanged = false

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        setListeners()

        return binding.root
    }

    private fun initialize(){
        binding.editTextManageQuestion.setText(question.content)
        if(question.pictureUrl != null){
            curImageUri = Uri.parse(question.pictureUrl)

            Glide.with(this)
                .load(question.pictureUrl)
                .error(R.drawable.add_picture)
                .fitCenter()
                .into(binding.imageViewManageQuestion)
        }

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                curImageUri = it.data?.data
                isImageChanged = true
                Glide.with(this)
                    .load(curImageUri)
                    .error(R.drawable.add_picture)
                    .fitCenter()
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