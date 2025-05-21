package com.btl.bookingHotel.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.btl.bookingHotel.model.RoomData
import com.btl.bookinghotel.databinding.ItemRoomBinding

class AvailableRoomAdapter(
    private val context: Context,
    private var rooms: MutableList<RoomData>
) : RecyclerView.Adapter<AvailableRoomAdapter.AvailableRoomViewHolder>() {

    private var selectedPosition = -1
    private var selectedRoomNumber: Int? = null

    inner class AvailableRoomViewHolder(val binding: ItemRoomBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableRoomViewHolder {
        return AvailableRoomViewHolder(
            ItemRoomBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int = rooms.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AvailableRoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.binding.tvRoomType.text = "Loại phòng: ${room.room_type}"
        holder.binding.tvRoomId.text = "Phòng số: ${room.room_number}"

        holder.binding.radioButton.isChecked = position == selectedPosition

        holder.binding.radioButton.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            val previousPosition = selectedPosition
            selectedPosition = adapterPosition
            selectedRoomNumber = rooms[adapterPosition].id_hotel_room

            if (previousPosition != -1) {
                notifyItemChanged(previousPosition)
            }
            notifyItemChanged(selectedPosition)

        }

        holder.binding.root.setOnClickListener {
            holder.binding.radioButton.performClick()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newRooms: List<RoomData>) {
        rooms.clear()
        rooms.addAll(newRooms)
        selectedPosition = -1
        selectedRoomNumber = null
        notifyDataSetChanged()
    }

    fun getSelectedRoomNumber(): Int? = selectedRoomNumber
}
