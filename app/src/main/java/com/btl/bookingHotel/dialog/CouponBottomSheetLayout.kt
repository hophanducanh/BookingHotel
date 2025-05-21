package com.btl.bookingHotel.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.btl.bookingHotel.adapter.MyCouponAdapter
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.model.MyDiscountResponse
import com.btl.bookinghotel.databinding.BottomLayoutCouponBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CouponBottomSheetLayout : BottomSheetDialogFragment() {
    private lateinit var binding: BottomLayoutCouponBinding
    private lateinit var adapter: MyCouponAdapter

    interface OnCouponSelectedListener {
        fun onCouponSelected(couponName: String, discountId: Int)
    }

    var listener: OnCouponSelectedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomLayoutCouponBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listItem.layoutManager = LinearLayoutManager(requireContext())
        binding.listItem.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

        ApiClient.create(requireContext()).getMyDiscount()
            .enqueue(object : Callback<MyDiscountResponse> {
                override fun onResponse(
                    call: Call<MyDiscountResponse>,
                    response: Response<MyDiscountResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val discountList = response.body()!!.data
                        adapter = MyCouponAdapter(discountList) { discount ->
                            listener?.onCouponSelected(discount.discount_name, discount.discount_id)
                            dismiss()
                        }
                        binding.listItem.adapter = adapter
                    } else {
                        Toast.makeText(context, "Failed to get discount", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MyDiscountResponse>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}