package com.btl.bookingHotel.component.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.btl.bookinghotel.databinding.FragmentFilterBinding

class FilterFragment : BaseFragment<FragmentFilterBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }

    override fun initView(view: View) {

    }

    override fun initListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterBinding {
        return FragmentFilterBinding.inflate(inflater)
    }
}