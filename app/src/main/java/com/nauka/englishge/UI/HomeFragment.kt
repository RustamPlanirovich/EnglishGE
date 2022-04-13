package com.nauka.englishge.UI

import android.animation.ObjectAnimator
import android.graphics.Color.red
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nauka.englishge.*
import com.nauka.englishge.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val records = ArrayList<Question?>()
    private val viewModel: HomeViewModel by activityViewModels()
    private var num: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.commonData.observe(viewLifecycleOwner) {
            for (elem in it) {
                records.add(elem)
                random()
                if (records.isNotEmpty()) {
                    binding.nextBtn.apply {
                        visible()
                        text = getString(R.string.start_button_text)
                    }
                    binding.transcriptionTV.gone()
                    binding.learnCB.gone()
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.viewUIState.collect {
                when (it) {
                    is LoginUIState.Success -> {
                        binding.timeCA.text = "close"
                        binding.timeCA.setTextColor(resources.getColor(R.color.all_button))
                    }
                    is LoginUIState.Loading ->
                        binding.timeCA.text = it.time

                }
            }
        }

        binding.timeCA.setOnClickListener {
            binding.correctAnswerCV.gone()
        }

        binding.learnCB.setOnCheckedChangeListener { compoundButton, b ->
            records[num]?.removeFromStudy = b
            lifecycleScope.launch(Dispatchers.IO) {
                records[num]?.let { viewModel.update(it) }
            }
        }

        binding.editCard.setOnClickListener {

            this.findNavController().navigate(R.id.action_homeFragment_to_editFragment)
        }


        binding.answerET.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN &&
                keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                binding.apply {
                    records[num]?.apply {
                        if (answerET.text.isNotEmpty()) {
                            nextBtn.visible()

                            if (answerET.text.toString().lowercase(Locale.getDefault()).trim() ==
                                sentenceInEnglish
                                    .lowercase(Locale.getDefault()).trim()
                            ) {
                                binding.transcriptionTV.visible()
                                countCorrectAnswers = countCorrectAnswers.plus(1)

                                totalCount = totalCount.plus(1)

                                lifecycleScope.launch(Dispatchers.IO) {
                                    viewModel.update(this@apply)
                                }

                                answerET.isEnabled = false
                                nextBtn.visible()
                                questionView.setTextColor(resources.getColor(android.R.color.holo_green_light))

                            } else {
                                binding.transcriptionTV.visible()
                                countIncorrectAnswers = countIncorrectAnswers.plus(1)

                                totalCount = totalCount.plus(1)

                                lifecycleScope.launch(Dispatchers.IO) {
                                    viewModel.update(this@apply)
                                }
                                answerET.isEnabled = false

                                correctAnswerCV.visible()
                                correctAnswer.text = records[num]?.sentenceInEnglish
                                viewModel.startTimeCounter()

                                questionView.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                            }
                        }
                    }
                }
                true
            } else
                false
        }


        binding.nextBtn.setOnClickListener {
            binding.editCard.visible()
            random()
            loadSentece()
            viewModel.mutableSelectedItem.value = records[num]
            Log.d("Hell", viewModel.mutableSelectedItem.value.toString())
        }
    }

    private fun loadSentece() {


        binding.apply {
            correctAnswerCV.gone()
            transcriptionTV.gone()
            nextBtn.text = getString(R.string.next_button_text)
            answerET.visible()
            learnCB.visible()

            questionView.setTextColor(resources.getColor(R.color.white))
            answerET.apply {
                isEnabled = true
                text.clear()
            }
            if (answerET.text.isEmpty()) {
                nextBtn.gone()
            }
            learnCB.isChecked = records[num]?.removeFromStudy!!
            questionView.text = records[num]?.sentenceInRussian
            transcriptionTV.text = records[num]?.transcription

            ObjectAnimator.ofInt(
                binding.learnPB,
                "progress",
                records[num]?.totalCount!!
            )
                .setDuration(100)
                .start()
        }

    }

    fun random() {
        num = (0..records.size).filter { it != num }.random()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDataFromDatabase()
    }

}