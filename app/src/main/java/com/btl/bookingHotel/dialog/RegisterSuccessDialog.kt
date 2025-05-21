package com.btl.bookingHotel.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.btl.bookinghotel.databinding.DialogRegisterSuccessBinding

class RegisterSuccessDialog(
    context: Context,
    style: Int,
) : BaseDialog<DialogRegisterSuccessBinding>(context, style) {

    override fun initView() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun initListener() {

    }

    override fun setBinding(): DialogRegisterSuccessBinding {
        return DialogRegisterSuccessBinding.inflate(layoutInflater)
    }
}
