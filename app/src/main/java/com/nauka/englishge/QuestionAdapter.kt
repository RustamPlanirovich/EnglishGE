package com.nauka.englishge

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nauka.englishge.databinding.QuestionItemBinding

class QuestionAdapter(private val cl: WallpaperClickListener) :
    PagingDataAdapter<Question, QuestionAdapter.ItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        val binding =
            QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewholder(
            binding
        )

    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        getItem(position)?.let { holder.bind(it, cl.clickListener) }
    }

    class ItemViewholder(private val binding: QuestionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Question, clickListener: (Question) -> Unit) = with(itemView) {
            // TODO: Bind the data with View
            binding.question.text = "Вопрос на английском: " + item.sentenceInEnglish
            binding.questionRus.text = "Вопрос на русском: " + item.sentenceInRussian
            binding.transcription.text = "Транскрипция: " + item.transcription
            binding.succesTV.text = "Верных ответов: " + item.countCorrectAnswers.toString()
            binding.errorTV.text = "Ошибок: " + item.countIncorrectAnswers.toString()
            binding.totalTV.text = "Всего показов: " + item.totalCount.toString()
            binding.learnPB.progress = item.totalCount
            binding.learnCB.isChecked = item.removeFromStudy

            itemView.setOnClickListener {
                clickListener(item)
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem.id == newItem.id ||
                oldItem.removeFromStudy == newItem.removeFromStudy ||
                oldItem.totalCount == newItem.totalCount ||
                oldItem.sentenceInEnglish == newItem.sentenceInEnglish ||
                oldItem.sentenceInRussian == newItem.sentenceInRussian ||
                oldItem.countCorrectAnswers == newItem.countCorrectAnswers ||
                oldItem.countIncorrectAnswers == newItem.countIncorrectAnswers ||
                oldItem.transcription == newItem.transcription
    }

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }
}