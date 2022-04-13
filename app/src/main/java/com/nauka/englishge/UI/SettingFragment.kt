package com.nauka.englishge.UI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nauka.englishge.*
import com.nauka.englishge.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class SettingFragment() : Fragment() {

    //Инжектим экземпляр базы данных
    @Inject
    lateinit var repository: Repository

    private lateinit var binding: FragmentSettingBinding
    private lateinit var adapter: QuestionAdapter


    private val viewModel: SettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        binding.questionRV.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        //Присваиваем адаптер

        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Создаем экземпляр адаптера
        adapter = QuestionAdapter(WallpaperClickListener {
            this.findNavController().navigate(R.id.action_settingFragment_to_editFragment)
            viewModel.mutableSelectedItem.value = it
        })



        binding.questionRV.adapter = adapter


        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.characters.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }


//            Snackbar.make(binding.root, "Hello", Snackbar.LENGTH_LONG).show()


        binding.searchByRoom.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                if (text.isNotEmpty()) {
                    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                        viewModel.charactersSeartch(text.toString()).collectLatest { pagingData ->
                            adapter.submitData(pagingData)
                            Log.d("Help", pagingData.toString())
                        }
                    }


                } else {
                    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                        viewModel.characters.collectLatest { pagingData ->
                            adapter.submitData(pagingData)
                        }
                    }
                }
            }
        }


        //Резульатат возвращаемый при выборае файла
        var resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data = result?.data?.data
                    viewModel.readDataFromJsonFile(data)
                }
            }

        //Событие происходит при клике на кнопку "Прочитать файл""
        binding.readFile.setOnClickListener {
            binding.readFile.setText("${adapter.itemCount}")
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
            resultLauncher.launch(intent)
        }
    }


}

