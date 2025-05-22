package com.btl.bookingHotel.component.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.btl.bookinghotel.R
import com.btl.bookinghotel.databinding.FragmentFilterBinding
import com.mohammedalaa.seekbar.DoubleValueSeekBarView
import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener

class FilterFragment : BaseFragment<FragmentFilterBinding>() {

    private val defaultMin = 100
    private val defaultMax = 5000

    private var starValue = 0
    private var ratingValue = 0
    private var minPrice = defaultMin
    private var maxPrice = defaultMax

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            starValue = it.getInt("star_value", 0)
            ratingValue = it.getInt("rating_value", 0)
            minPrice = it.getInt("min_price", defaultMin)
            maxPrice = it.getInt("max_price", defaultMax)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRangeSeekBar()
        initView(view)
        initListener()
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        binding.rangeSeekbar.minValue = 100
        binding.rangeSeekbar.maxValue = 5000

        // Set giá trị seekbar đã lưu
        binding.rangeSeekbar.currentMinValue = minPrice
        binding.rangeSeekbar.currentMaxValue = maxPrice

        binding.tvMinPrice.text = formatPrice(minPrice)
        binding.tvMaxPrice.text = formatPrice(maxPrice)

        // Set radioGroupStar theo giá trị đã lưu
        when (starValue) {
            1 -> binding.radioGroupStar.check(R.id.btn_one_star)
            2 -> binding.radioGroupStar.check(R.id.btn_two_star)
            3 -> binding.radioGroupStar.check(R.id.btn_three_star)
            4 -> binding.radioGroupStar.check(R.id.btn_four_star)
            5 -> binding.radioGroupStar.check(R.id.btn_five_Star)
            else -> binding.radioGroupStar.clearCheck()
        }

        // Set radioGroupRating theo giá trị đã lưu
        when (ratingValue) {
            5 -> binding.radioGroupRating.check(R.id.btn_five)
            6 -> binding.radioGroupRating.check(R.id.btn_six)
            7 -> binding.radioGroupRating.check(R.id.btn_seven)
            8 -> binding.radioGroupRating.check(R.id.btn_eight)
            9 -> binding.radioGroupRating.check(R.id.btn_nine)
            else -> binding.radioGroupRating.clearCheck()
        }
    }

    override fun initListener() {
        binding.btnClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.radioGroupStar.setOnCheckedChangeListener { _, checkedId ->
            starValue = when (checkedId) {
                R.id.btn_one_star -> 1
                R.id.btn_two_star -> 2
                R.id.btn_three_star -> 3
                R.id.btn_four_star -> 4
                R.id.btn_five_Star -> 5
                else -> 0
            }
        }

        binding.radioGroupRating.setOnCheckedChangeListener { _, checkedId ->
            ratingValue = when (checkedId) {
                R.id.btn_five -> 5
                R.id.btn_six -> 6
                R.id.btn_seven -> 7
                R.id.btn_eight -> 8
                R.id.btn_nine -> 9
                else -> 0
            }
        }

        binding.btnReset.setOnClickListener {
            binding.radioGroupStar.clearCheck()
            binding.radioGroupRating.clearCheck()

            minPrice = defaultMin
            maxPrice = defaultMax
            binding.rangeSeekbar.currentMinValue = minPrice
            binding.rangeSeekbar.currentMaxValue = maxPrice

            binding.tvMinPrice.text = formatPrice(minPrice)
            binding.tvMaxPrice.text = formatPrice(maxPrice)

            starValue = 0
            ratingValue = 0
        }

        binding.btnApply.setOnClickListener {
            val result = Bundle().apply {
                putInt("min_price", minPrice * 1000)
                putInt("max_price", maxPrice * 1000)
                putInt("star", starValue)
                putInt("rating", ratingValue)
            }

            parentFragmentManager.setFragmentResult("filter_result", result)
            parentFragmentManager.popBackStack()
        }
    }

    private fun initRangeSeekBar() {
        binding.rangeSeekbar.setOnRangeSeekBarViewChangeListener(object :
            OnDoubleValueSeekBarChangeListener {
            override fun onValueChanged(seekBar: DoubleValueSeekBarView?, min: Int, max: Int, fromUser: Boolean) {
                minPrice = min
                maxPrice = max
                binding.tvMinPrice.text = formatPrice(min)
                binding.tvMaxPrice.text = formatPrice(max)
            }

            override fun onStartTrackingTouch(seekBar: DoubleValueSeekBarView?, min: Int, max: Int) {}
            override fun onStopTrackingTouch(seekBar: DoubleValueSeekBarView?, min: Int, max: Int) {}
        })
    }

    @SuppressLint("DefaultLocale")
    private fun formatPrice(value: Int): String {
        return String.format("%,d", value * 1000).replace(',', '.')
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterBinding {
        return FragmentFilterBinding.inflate(inflater)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("star_value", starValue)
        outState.putInt("rating_value", ratingValue)
        outState.putInt("min_price", minPrice)
        outState.putInt("max_price", maxPrice)
    }
}
