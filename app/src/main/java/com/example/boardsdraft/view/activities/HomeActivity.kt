package com.example.boardsdraft.view.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.ActivityHomeBinding
import com.example.boardsdraft.view.fragments.BoardsFragment
import com.example.boardsdraft.view.fragments.MyProfileFragment
import com.example.boardsdraft.view.fragments.MyTasksFragment
import com.example.boardsdraft.view.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
//    private var currentFragmentTag: String? = null
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(viewModel.currentFragment != null){
            if(viewModel.currentFragment == "BOARDS") replaceFragment(BoardsFragment(), R.id.home, "BOARDS")
            if(viewModel.currentFragment == "MY_TASKS") replaceFragment(MyTasksFragment(), R.id.home, "MY_TASKS")
            else replaceFragment(MyProfileFragment(), R.id.home, "MY_PROFILE")
        }
        else{
            replaceFragment(BoardsFragment(), R.id.home, "BOARDS")
        }
        bottomNav()
    }

    private fun bottomNav() {
        binding.bottomNavBar.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.bottom_home -> {
                    if(viewModel.currentFragment == "BOARDS") return@setOnItemSelectedListener true
                    replaceFragment(BoardsFragment(), R.id.home, "BOARDS")
                }
                R.id.bottom_my_tasks -> {
                    if(viewModel.currentFragment == "MY_TASKS") return@setOnItemSelectedListener true
                    replaceFragment(MyTasksFragment(), R.id.home, "MY_TASKS")
                }
                R.id.bottom_my_profile -> {
                    if(viewModel.currentFragment == "MY_PROFILE") return@setOnItemSelectedListener true
                    replaceFragment(MyProfileFragment(), R.id.home, "MY_PROFILE")
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, containerId: Int, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment, tag)
            .commit()
        viewModel.currentFragment = tag
    }


}