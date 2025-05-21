package com.btl.bookingHotel.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.model.LocationData
import com.btl.bookingHotel.model.LocationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.btl.bookinghotel.R
import com.btl.bookinghotel.databinding.DialogSearchBinding

class SearchDialog : DialogFragment() {
    private lateinit var binding: DialogSearchBinding
    private var locationId: Int = -1
    private var address: String = ""
    private var hotelName: String = ""
    private var locationList: List<LocationData> = listOf()

    private var listener: OnSearchListener? = null

    companion object {
        fun newInstance(locationId: Int, address: String, hotelName: String): SearchDialog {
            val args = Bundle().apply {
                putInt("locationId", locationId)
                putString("address", address)
                putString("hotelName", hotelName)
            }
            val fragment = SearchDialog()
            fragment.arguments = args
            return fragment
        }
    }

    interface OnSearchListener {
        fun onSearch(locationId: Int, address: String, hotelName: String)
    }

    fun setOnSearchListener(listener: OnSearchListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationId = arguments?.getInt("locationId") ?: -1
        address = arguments?.getString("address") ?: ""
        hotelName = arguments?.getString("hotelName") ?: ""

        binding.etAddress.setText(address)
        binding.etHotelName.setText(hotelName)
        fetchCities()

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnSearch.setOnClickListener {
            val selectedCity = binding.spinnerCity.selectedItem?.toString()
            val selectedLocationId = locationList.find { it.city == selectedCity }?.id_location ?: -1
            val inputAddress = binding.etAddress.text.toString()
            val inputHotelName = binding.etHotelName.text.toString()

            listener?.onSearch(selectedLocationId, inputAddress, inputHotelName)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.TOP)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.DialogSlideDownAnimation
        }
    }

    private fun fetchCities() {
        ApiClient.create(requireContext()).getLocations()
            .enqueue(object : Callback<LocationResponse> {
                override fun onResponse(
                    call: Call<LocationResponse>,
                    response: Response<LocationResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status == "success") {
                        val data = response.body()?.data ?: listOf()
                        locationList = data
                        val cities = data.map { it.city }

                        val adapter = object : ArrayAdapter<String>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            cities
                        ) {
                            override fun getDropDownView(
                                position: Int,
                                convertView: View?,
                                parent: ViewGroup
                            ): View {
                                val view = super.getDropDownView(position, convertView, parent)
                                val textView = view as TextView
                                textView.setTextColor(Color.BLACK)
                                return view
                            }
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerCity.adapter = adapter

                        val selectedLocation = data.find { it.id_location == locationId }
                        val index = cities.indexOfFirst { it == selectedLocation?.city }
                        if (index >= 0) {
                            binding.spinnerCity.setSelection(index)
                        }

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Không lấy được danh sách thành phố",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
