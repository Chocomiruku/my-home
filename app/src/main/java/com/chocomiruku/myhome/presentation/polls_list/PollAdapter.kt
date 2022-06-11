package com.chocomiruku.myhome.presentation.polls_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getColorStateList
import androidx.core.content.res.ResourcesCompat.getFont
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.databinding.PollListItemBinding
import com.chocomiruku.myhome.domain.models.Poll
import com.chocomiruku.myhome.util.generateRandomColor
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PollAdapter(
    private val isAdmin: Boolean?,
    private val onVote: (pollId: String, selectedOptions: List<String>) -> Unit,
    private val onClose: (pollId: String) -> Unit
) :
    ListAdapter<Poll, PollAdapter.ViewHolder>(PollDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val poll = getItem(position)
        holder.bind(poll, isAdmin, onVote, onClose)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(
        private val binding: PollListItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            poll: Poll,
            isAdmin: Boolean?,
            onVote: (pollId: String, selectedOptions: List<String>) -> Unit,
            onClose: (pollId: String) -> Unit
        ) = with(binding) {
            titleText.text = poll.title

            if (poll.hasVoted == true || poll.closed) {
                closedLabel.isVisible = poll.closed
                hideVoteOptions()
                bindChart(poll)
            } else {
                bindOptions(poll, onVote)
            }
            if (isAdmin == true) {
                closeBtn.apply {
                    isVisible = !poll.closed
                    setOnClickListener { showAlertDialog { onClose(poll.pollId) } }
                }
            }
        }

        private fun bindChart(poll: Poll) {
            val options = poll.options.keys.toList()
            styleChart(options)
            val voteValues = mutableListOf<BarEntry>()

            poll.options.forEach { (option, votes) ->
                voteValues.add(BarEntry(options.indexOf(option).toFloat(), votes.toFloat()))
            }

            val set = BarDataSet(voteValues, binding.root.context.getString(R.string.poll_results))
            set.apply {
                color = binding.root.context.getColor(generateRandomColor())
                setDrawValues(false)
            }

            val dataSets = BarData(set)

            binding.voteChart.apply {
                data = dataSets
                isVisible = true
                animateXY(1500, 1500)
            }
        }

        private fun styleChart(options: List<String>) {
            val chart = binding.voteChart

            chart.apply {
                setDrawGridBackground(false)
                setPinchZoom(false)
                axisLeft.isEnabled = false
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(false)
            }

            chart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter =
                    IndexAxisValueFormatter(options)
                axisMinimum = -1F
                axisMaximum = options.size + 0f
                isGranularityEnabled = true
                granularity = 1f
                labelCount = options.size + 1
                setDrawGridLines(false)
                typeface = getFont(binding.root.context, R.font.proximanova_bold)
                textSize = 14f
                textColor = getColor(binding.root.context, R.color.gray_500)
            }

            val yAxis = chart.axisRight
            yAxis.apply {
                isGranularityEnabled = true
                granularity = 1f
                setDrawGridLines(false)
                typeface = getFont(binding.root.context, R.font.proximanova_regular)
                textSize = 16f
            }
        }

        private fun bindOptions(
            poll: Poll,
            onVote: (pollId: String, selectedOptions: List<String>) -> Unit
        ) = with(binding) {
            val checkboxes = listOf(
                checkbox1,
                checkbox2,
                checkbox3,
                checkbox4,
                checkbox5
            )
            val radioButtons = listOf(
                radioButton1,
                radioButton2,
                radioButton3,
                radioButton4,
                radioButton5
            )

            if (poll.multipleChoice) {
                checkboxesGroup.isVisible = true
                fillViews(poll, checkboxes)

                voteBtn.setOnClickListener {
                    onVote(poll.pollId, getSelectedOptions(checkboxes))
                }
            } else {
                radioGroup.isVisible = true
                fillViews(poll, radioButtons)

                voteBtn.setOnClickListener {
                    onVote(poll.pollId, getSelectedOptions(radioButtons))
                }
            }
        }

        private fun fillViews(poll: Poll, views: List<View>) {
            val options = poll.options.keys.sortedBy { it.first() }

            views.zip(options).forEach { (view, option) ->
                when (view) {
                    is CheckBox -> view.text = option
                    is RadioButton -> view.text = option
                }
                view.isVisible = true
            }
        }

        private fun getSelectedOptions(views: List<View>): List<String> {
            return views.map { view ->
                when (view) {
                    is CheckBox -> if (view.isChecked) view.text.toString() else ""
                    is RadioButton -> if (view.isChecked) view.text.toString() else ""
                    else -> ""
                }
            }.filterNot { option ->
                option == ""
            }
        }

        private fun showAlertDialog(onAccept: () -> Unit) {
            MaterialAlertDialogBuilder(binding.root.context)
                .setTitle(R.string.confirm_close)
                .setMessage(R.string.close_question)
                .setNeutralButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(R.string.accept) { _, _ ->
                    onAccept()
                }
                .show()
        }

        private fun hideVoteOptions() = with(binding) {
            radioGroup.isVisible = false
            checkboxesGroup.isVisible = false
            voteBtn.isVisible = false
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    PollListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class PollDiffCallback :
        DiffUtil.ItemCallback<Poll>() {

        override fun areItemsTheSame(oldItem: Poll, newItem: Poll): Boolean {
            return oldItem.pollId == newItem.pollId
        }

        override fun areContentsTheSame(oldItem: Poll, newItem: Poll): Boolean {
            return oldItem == newItem
        }
    }
}