package com.globant.harrypotterapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.globant.harrypotterapp.R
import com.globant.harrypotterapp.adapter.ViewPagerAdapter
import com.globant.harrypotterapp.databinding.ActivityHouseBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_house.tabLayout

class HouseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHouseBinding
    private var houseName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHouseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        houseName = intent.extras?.getString(HOUSE_NAME)
        binding.activityHouseAppBarTitle.text = houseName
        initViewPager()
    }

    private fun initViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        val tabLayout: TabLayout = tabLayout
        val listOfFragmentNames: List<String> = listOf(getString(R.string.house_detail_tab), getString(R.string.characters_tab))
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = listOfFragmentNames[position]
        }.attach()
    }

    companion object {
        private const val HOUSE_NAME = "HouseName"
        fun getIntent(activity: Activity): Intent = Intent(activity, HouseActivity::class.java)
    }
}
