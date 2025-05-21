package com.btl.bookingHotel.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.btl.bookinghotel.R
import com.btl.bookinghotel.databinding.BottomLayoutSortBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortBottomSheetLayout : BottomSheetDialogFragment() {
    private lateinit var binding: BottomLayoutSortBinding
    private var selectedSortType: SortType? = null

    interface OnSortSelectedListener {
        fun onSortSelected(sortType: SortType)
    }

    enum class SortType {
        PRICE_ASC, PRICE_DESC, RATING, STAR_ASC, STAR_DESC
    }

    private var listener: OnSortSelectedListener? = null

    fun setOnSortSelectedListener(listener: OnSortSelectedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomLayoutSortBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.windowAnimations = R.style.DialogAnimation
            setGravity(Gravity.BOTTOM)
        }

        when (selectedSortType) {
            SortType.PRICE_ASC -> binding.btnPriceAsc.isChecked = true
            SortType.PRICE_DESC -> binding.btnPriceDesc.isChecked = true
            SortType.RATING -> binding.btnRating.isChecked = true
            SortType.STAR_DESC -> binding.btnStarDesc.isChecked = true
            SortType.STAR_ASC -> binding.btnStarAsc.isChecked = true
            else -> {}
        }

        binding.btnPriceAsc.setOnClickListener {
            selectedSortType = SortType.PRICE_ASC
            listener?.onSortSelected(SortType.PRICE_ASC)
            dismiss()
        }

        binding.btnPriceDesc.setOnClickListener {
            selectedSortType = SortType.PRICE_DESC
            listener?.onSortSelected(SortType.PRICE_DESC)
            dismiss()
        }

        binding.btnRating.setOnClickListener {
            selectedSortType = SortType.RATING
            listener?.onSortSelected(SortType.RATING)
            dismiss()
        }

        binding.btnStarAsc.setOnClickListener {
            selectedSortType = SortType.STAR_ASC
            listener?.onSortSelected(SortType.STAR_ASC)
            dismiss()
        }

        binding.btnStarDesc.setOnClickListener {
            selectedSortType = SortType.STAR_DESC
            listener?.onSortSelected(SortType.STAR_DESC)
            dismiss()
        }
    }

    fun setInitialSortType(type: SortType?) {
        selectedSortType = type
    }
}