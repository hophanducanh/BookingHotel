package com.btl.bookingHotel.component.fragment

import android.R
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.btl.bookingHotel.adapter.HighestHotelAdapter
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.component.activity.SearchActivity
import com.btl.bookingHotel.model.LocationData
import com.btl.bookingHotel.model.LocationResponse
import com.btl.bookingHotel.utils.HotelViewModel
import com.btl.bookinghotel.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private var locationList: List<LocationData> = listOf()

    private lateinit var hanoiAdapter: HighestHotelAdapter
    private lateinit var nhatrangAdapter: HighestHotelAdapter
    private lateinit var hcmAdapter: HighestHotelAdapter

    override fun initView(view: View) {
        fetchCities()
        hanoiAdapter = HighestHotelAdapter(requireContext(), mutableListOf())
        binding.listHighestHotelInHanoi.adapter = hanoiAdapter
        setupHotelList(4, hanoiAdapter)

        nhatrangAdapter = HighestHotelAdapter(requireContext(), mutableListOf())
        binding.listHighestHotelInNhatrang.adapter = nhatrangAdapter
        setupHotelList(9, nhatrangAdapter)

        hcmAdapter = HighestHotelAdapter(requireContext(), mutableListOf())
        binding.listHighestHotelInHcm.adapter = hcmAdapter
        setupHotelList(6, hcmAdapter)
    }


    override fun initListener() {
        binding.btnSearch.setOnClickListener {
            val cityPosition = binding.spinnerCity.selectedItemPosition
            if (cityPosition == 0) {
                Toast.makeText(requireContext(), "Vui lòng chọn tỉnh/thành phố", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val selectedCity = binding.spinnerCity.selectedItem.toString()
            val selectedLocation = locationList.find { it.city == selectedCity }
            val locationId = selectedLocation?.id_location

            val address = binding.etAddress.text.toString().trim()
            val hotelName = binding.etHotelName.text.toString().trim()

            if (locationId == null) {
                Toast.makeText(requireContext(), "Không tìm thấy ID của tỉnh", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val intent = Intent(requireContext(), SearchActivity::class.java).apply {
                putExtra("locationId", locationId)
                putExtra("address", address)
                putExtra("hotelName", hotelName)
            }
            startActivity(intent)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
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
                        val cityListWithHint = listOf("Chọn tỉnh/thành phố") + cities

                        val adapter = object : ArrayAdapter<String>(
                            requireContext(),
                            R.layout.simple_spinner_item,
                            cityListWithHint
                        ) {
                            override fun isEnabled(position: Int): Boolean {
                                return position != 0
                            }

                            override fun getDropDownView(
                                position: Int,
                                convertView: View?,
                                parent: ViewGroup
                            ): View {
                                val view = super.getDropDownView(position, convertView, parent)
                                val textView = view as TextView
                                textView.setTextColor(
                                    if (position == 0) android.graphics.Color.GRAY
                                    else android.graphics.Color.BLACK
                                )
                                return view
                            }
                        }

                        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                        binding.spinnerCity.adapter = adapter
                        binding.spinnerCity.setSelection(0)


                    }
                }

                override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupHotelList(locationId: Int, adapter: HighestHotelAdapter) {
        HotelViewModel(requireContext()).fetchHotels(
            locationId = locationId,
            sortBy = "hotel_star",
            sortOrder = "desc",
            paging = false,
            onSuccess = { hotels -> adapter.setData(hotels.take(8)) },
            onError = { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        )
    }
}