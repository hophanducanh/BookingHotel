package com.btl.bookingHotel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.btl.bookinghotel.databinding.CalendarCellBinding

class CalendarAdapter(
    private val daysOfMonth: List<String>
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var onItemClick: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClick = listener
    }

    inner class CalendarViewHolder(private val binding: CalendarCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: String) {
            binding.cellDay.text = day
            binding.root.setOnClickListener {
                if (day.isNotEmpty()) {
                    onItemClick?.invoke(day)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(daysOfMonth[position])
    }

    override fun getItemCount(): Int = daysOfMonth.size
}
