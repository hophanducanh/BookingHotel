package com.btl.bookingHotel.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.viewbinding.ViewBinding

abstract class BaseDialog<T : ViewBinding>(context: Context, style: Int) : Dialog(context, style) {

    protected lateinit var binding: T
    protected abstract fun initView()
    protected abstract fun initListener()
    protected abstract fun setBinding(): T

    private val flags: Int = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    override fun show() {
        setOnKeyListener { _, keyCode, _ ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )

        window?.decorView?.systemUiVisibility = flags
        super.show()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setBinding()
        setContentView(binding.root)
        initView()
        initListener()
    }

    fun setCancelable() {
        setCancelable(false)
    }

    fun setGravity(gravity: Int) {
        window?.setGravity(gravity)
    }
}