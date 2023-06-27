package com.example.boardsdraft.view.activities

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.ActivityManageProfileBinding
import com.example.boardsdraft.view.fragments.EditMyProfileFragment
import com.example.boardsdraft.view.fragments.ShowProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.measureNanoTime

@AndroidEntryPoint
class ManageProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.toolbar.apply{
            title = "My Profile"
            setNavigationOnClickListener {
                val fragmentManager = supportFragmentManager
                val backStackEntryCount = fragmentManager.backStackEntryCount
                if (backStackEntryCount > 0){
                    val currentFragmentTag = fragmentManager.getBackStackEntryAt(backStackEntryCount - 1).name
                    val editMyProfileFragment = EditMyProfileFragment::class.java.simpleName
                    if (currentFragmentTag == editMyProfileFragment){
                        fragmentManager.popBackStack()

                    }else{
                        finish()
                    }
                }
                else{
                    finish()
                }
            }
            inflateMenu(R.menu.profile_menu_item)

            setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.edit -> {
                       supportFragmentManager.beginTransaction()
                           .replace(binding.profileFragmentContainer.id,EditMyProfileFragment())
                           .addToBackStack("EditMyProfileFragment")
                           .commit()
                    }
                }
                true
            }


        }



        val bundle = Bundle()
        val showProfile = ShowProfileFragment("YES")
        bundle.putInt("userID",intent.getIntExtra("currentUserID",0))

        showProfile.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.profile_fragment_container,showProfile)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val backStackEntryCount = fragmentManager.backStackEntryCount

        if (backStackEntryCount > 0) {
            val currentFragmentTag = fragmentManager.getBackStackEntryAt(backStackEntryCount - 1).name
            val editMyProfileFragment = EditMyProfileFragment::class.java.simpleName

            if (currentFragmentTag == editMyProfileFragment) {
                fragmentManager.popBackStack()
            } else {
                finish()
            }
        } else {
            super.onBackPressed()
        }
    }



}