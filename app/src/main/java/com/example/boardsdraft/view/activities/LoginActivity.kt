package com.example.boardsdraft.view.activities

import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.boardsDraft.databinding.ActivityLoginBinding
import com.example.boardsdraft.view.adapter.LoginAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginPageAppName.typeface = Typeface.createFromAsset(assets, "unfoldingfont.ttf")

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sign In"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sign Up"))

        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        binding.viewPager.adapter = LoginAdapter(supportFragmentManager, lifecycle)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    binding.viewPager.currentItem = it.position
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }



    override fun onBackPressed() {
        if (binding.viewPager.currentItem == 0) {
            onBackPressedDispatcher.onBackPressed()
        } else {
            binding.viewPager.currentItem = binding.viewPager.currentItem - 1
        }
    }
}
