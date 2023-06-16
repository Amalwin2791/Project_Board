package com.example.boardsdraft.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.ActivityTasksBinding
import com.example.boardsdraft.view.fragments.MyDialogFragment
import com.example.boardsdraft.view.fragments.TasksOfProjectFragment
import com.example.boardsdraft.view.viewModel.BoardsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksActivity : AppCompatActivity(),MyDialogFragment.OnItemClickListener{

    private lateinit var binding: ActivityTasksBinding
    private val viewModel: BoardsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.apply {

            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.members -> {
                        startActivity(
                            Intent(
                                this@TasksActivity,
                                MembersActivity::class.java
                            ).apply {
                                putExtra("projectName", intent.getStringExtra("projectName"))
                                putExtra("projectID", intent.getIntExtra("projectID", 0))
                                putExtra("projectCode",intent.getStringExtra("projectCode"))
                            })
                    }

                    R.id.leaveBoard -> {
                        MyDialogFragment("Are You Sure You Want To Leave this Board?","Yes",
                            this@TasksActivity)
                            .show(supportFragmentManager,"deleteDialog")
                    }
                }
                true
            }
            title = intent.getStringExtra("projectName")
            inflateMenu(R.menu.menu_for_tasks)
            setNavigationOnClickListener {
                val fragmentManager = supportFragmentManager
                val backStackEntryCount = fragmentManager.backStackEntryCount

                if (backStackEntryCount > 0) {
                    val currentFragmentTag = fragmentManager.getBackStackEntryAt(backStackEntryCount - 1).name
                    val tasksOfProjectFragmentTag = TasksOfProjectFragment::class.java.simpleName

                    if (currentFragmentTag == tasksOfProjectFragmentTag) {
                        finish()
                    } else {
                        fragmentManager.popBackStackImmediate()
                    }
                } else {
                    finish()
                }
            }
            val bundle = Bundle()
            val tasksOfProjectFragment = TasksOfProjectFragment()
            bundle.putInt("projectID", intent.getIntExtra("projectID", 0))
            tasksOfProjectFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(binding.tasksView.id, tasksOfProjectFragment).commit()
        }

    }

    override fun result(choice: String) {
        if(choice == "YES"){
            viewModel.deleteUserProjectCrossRef(
                viewModel.getCurrentUserID(),
                intent.getIntExtra("projectID", 0)
            )
            finish()
        }
    }

}


