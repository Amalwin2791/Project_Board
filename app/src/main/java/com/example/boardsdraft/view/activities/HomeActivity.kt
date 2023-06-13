package com.example.boardsdraft.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.ActivityHomeBinding
import com.example.boardsdraft.view.fragments.BoardsFragment
import com.example.boardsdraft.view.fragments.MyProfileFragment
import com.example.boardsdraft.view.fragments.MyTasksFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private var currentFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(binding.home.id, BoardsFragment()).commit()
        bottomNav()
    }

    private fun bottomNav() {
        binding.bottomNavBar.setOnItemSelectedListener { item ->
            val fragmentTag = item.itemId.toString()
            if (fragmentTag == currentFragmentTag) {
                return@setOnItemSelectedListener true
            }

            when (item.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(BoardsFragment(), R.id.home, fragmentTag)
                }
                R.id.bottom_my_tasks -> {
                    replaceFragment(MyTasksFragment(), R.id.home, fragmentTag)
                }
                R.id.bottom_my_profile -> {
                    replaceFragment(MyProfileFragment(), R.id.home, fragmentTag)
                }
            }
            currentFragmentTag = fragmentTag
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, containerId: Int, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment, tag)
            .commit()
    }


}