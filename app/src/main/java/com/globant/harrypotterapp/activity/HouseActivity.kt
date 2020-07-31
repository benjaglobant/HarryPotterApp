package com.globant.harrypotterapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.globant.harrypotterapp.R
import com.globant.harrypotterapp.adapter.ViewPagerAdapter
import com.globant.harrypotterapp.databinding.ActivityHouseBinding
import com.globant.harrypotterapp.viewmodel.HouseDetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class HouseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHouseBinding
    private lateinit var houseName: String
    private val houseDetailViewModel by viewModel<HouseDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHouseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.getStringExtra(HOUSE_NAME)?.let { houseName = it }
        initUI()
        houseDetailViewModel.fetchHouseDetail(houseName)
    }

    private fun initUI() {
        binding.activityHouseAppBarTitle.text = houseName
        initViewPager()
    }

    private fun initViewPager() {
        val viewPager: ViewPager2 = binding.activityHouseViewPager
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, houseName)

        val tabLayout: TabLayout = binding.activityHouseTabLayout
        val listOfFragmentNames: List<String> = listOf(getString(R.string.house_detail_tab), getString(R.string.characters_tab))
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = listOfFragmentNames[position]
        }.attach()
    }

    companion object {
        const val HOUSE_NAME = "HouseName"
        fun getIntent(activity: Activity, houseName: String): Intent =
            Intent(activity, HouseActivity::class.java).putExtra(HOUSE_NAME, houseName)
    }
}
