package com.example.boardsdraft.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.ActivityManageProfileBinding
import com.example.boardsdraft.view.fragments.ShowProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileToolbar.apply{
            title = "My Profile"
            setNavigationOnClickListener {
                finish()
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
}