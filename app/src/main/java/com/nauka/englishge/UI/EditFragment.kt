package com.nauka.englishge.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nauka.englishge.Question
import com.nauka.englishge.R
import com.nauka.englishge.databinding.FragmentEditBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditFragment : Fragment() {
    private lateinit var binding: FragmentEditBinding

    private val viewModel: SettingViewModel by activityViewModels()
    private val viewModelHome: HomeViewModel by activityViewModels()
    private val viewModels: EditViewModel by activityViewModels()
    lateinit var question: Question

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            viewModel.mutableSelectedItem.observe(viewLifecycleOwner) {
                binding.sentenceInEnglish.setText(it.sentenceInEnglish)
                binding.sentenceInRussian.setText(it.sentenceInRussian)
                binding.learn.isChecked = it.removeFromStudy
                question = it
            }

            viewModelHome.mutableSelectedItem.observe(viewLifecycleOwner) {
                binding.sentenceInEnglish.setText(it.sentenceInEnglish)
                binding.sentenceInRussian.setText(it.sentenceInRussian)
                binding.learn.isChecked = it.removeFromStudy
                question = it
            }








        binding.saveBtn.setOnClickListener {
            question.sentenceInEnglish = binding.sentenceInEnglish.text.toString()
            question.sentenceInRussian = binding.sentenceInRussian.text.toString()
            question.removeFromStudy = binding.learn.isChecked


            viewModels.update(question)

            this.findNavController().navigate(R.id.action_editFragment_to_settingFragment)

        }
    }

}