package com.btl.bookingHotel.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.btl.bookinghotel.databinding.DialogConfirmBookingBinding
import com.btl.bookinghotel.databinding.DialogRegisterSuccessBinding

class ConfirmBookingDialog(
    context: Context,
    style: Int = 0,
    private val onConfirm: () -> Unit,
    private val onCancel: () -> Unit
) : BaseDialog<DialogConfirmBookingBinding>(context, style) {

    override fun initView() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun initListener() {
        binding.btnConfirm.setOnClickListener {
            onConfirm()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            onCancel()
            dismiss()
        }
    }

    override fun setBinding(): DialogConfirmBookingBinding {
        return DialogConfirmBookingBinding.inflate(layoutInflater)
    }
}

