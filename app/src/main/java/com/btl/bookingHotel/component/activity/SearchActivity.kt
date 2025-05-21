package com.btl.bookingHotel.component.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.btl.bookingHotel.adapter.SearchHotelAdapter
import com.btl.bookingHotel.component.fragment.FilterFragment
import com.btl.bookingHotel.dialog.SearchDialog
import com.btl.bookingHotel.dialog.SortBottomSheetLayout
import com.btl.bookingHotel.utils.HotelViewModel
import com.btl.bookinghotel.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity<ActivitySearchBinding>() {
    private lateinit var adapter: SearchHotelAdapter
    private var locationId: Int = -1
    private var address: String = ""
    private var hotelName: String = ""
    private var currentSortType: SortBottomSheetLayout.SortType? = null

    override fun initView() {
        locationId = intent?.getIntExtra("locationId", -1) ?: -1
        address = intent?.getStringExtra("address") ?: ""
        hotelName = intent?.getStringExtra("hotelName") ?: ""
        adapter = SearchHotelAdapter(this, mutableListOf())
        binding.listItem.adapter = adapter
        binding.listItem.layoutManager = LinearLayoutManager(this)
        binding.listItem.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun initData() {
        performSearch(locationId, address, hotelName)
    }

    override fun initListener() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        adapter.setOnItemClickListener { hotelData ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("hotelData", hotelData)
            startActivity(intent)
        }

        binding.btnChange.setOnClickListener {
            val dialog = SearchDialog.newInstance(locationId, address, hotelName)
            dialog.setOnSearchListener(object : SearchDialog.OnSearchListener {
                override fun onSearch(locationId: Int, address: String, hotelName: String) {
                    this@SearchActivity.locationId = locationId
                    this@SearchActivity.address = address
                    this@SearchActivity.hotelName = hotelName

                    performSearch(
                        this@SearchActivity.locationId,
                        this@SearchActivity.address, this@SearchActivity.hotelName
                    )
                }
            })
            dialog.show(supportFragmentManager, "SearchDialog")
        }

        binding.btnSort.setOnClickListener {
            val sortLayout = SortBottomSheetLayout()
            sortLayout.setInitialSortType(currentSortType)
            sortLayout.setOnSortSelectedListener(object :
                SortBottomSheetLayout.OnSortSelectedListener {
                override fun onSortSelected(sortType: SortBottomSheetLayout.SortType) {
                    currentSortType = sortType
                    sortHotels(sortType)
                }
            })
            sortLayout.show(supportFragmentManager, "SortBottomSheet")
        }

        binding.btnFilter.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, FilterFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun sortHotels(sortType: SortBottomSheetLayout.SortType) {
        val (sortBy, sortOrder) = when (sortType) {
            SortBottomSheetLayout.SortType.PRICE_ASC -> "new_price" to "asc"
            SortBottomSheetLayout.SortType.PRICE_DESC -> "new_price" to "desc"
            SortBottomSheetLayout.SortType.RATING -> "hotel_rating" to "desc"
            SortBottomSheetLayout.SortType.STAR_ASC -> "hotel_star" to "asc"
            SortBottomSheetLayout.SortType.STAR_DESC -> "hotel_star" to "desc"
        }

        performSearch(locationId, address, hotelName, sortBy, sortOrder)
    }

    @SuppressLint("SetTextI18n")
    private fun performSearch(
        locationId: Int, address: String, hotelName: String, sortBy: String? = null,
        sortOrder: String? = null
    ) {

        HotelViewModel(this).fetchHotels(
            locationId = locationId,
            address = address,
            hotelName = hotelName,
            sortBy = sortBy,
            sortOrder = sortOrder,
            paging = false,
            onSuccess = { hotels ->
                adapter.setData(hotels)
                binding.tvResult.text = "${hotels.size} kết quả"
            },
            onError = { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                binding.tvResult.text = "0 kết quả"
            }
        )
    }

    override fun getViewBinding(): ActivitySearchBinding {
        return ActivitySearchBinding.inflate(layoutInflater)
    }

}