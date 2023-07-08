package com.example.boardsdraft.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.ActivityTasksBinding
import com.example.boardsdraft.view.fragments.EditBoardFragment
import com.example.boardsdraft.view.fragments.MyDialogFragment
import com.example.boardsdraft.view.fragments.TasksOfProjectFragment
import com.example.boardsdraft.view.viewModel.TaskActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksActivity : AppCompatActivity(),MyDialogFragment.OnItemClickListener{

    private lateinit var binding: ActivityTasksBinding
    private val viewModel: TaskActivityViewModel by viewModels()
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
                                putExtra("projectCreatedByID",intent.getIntExtra("projectCreatedByID",0))
                            })
                    }

                    R.id.leaveBoard -> {
                        MyDialogFragment("Are You Sure You Want To Leave this Board?","Yes",
                            this@TasksActivity)
                            .show(supportFragmentManager,"deleteDialog")

                    }


                    R.id.edit_board -> {
                        
                        if(viewModel.getCurrentUserID() == intent.getIntExtra("projectCreatedByID",0)){

                            val bundle = Bundle()
                            bundle.putInt("projectID", intent.getIntExtra("projectID", 0))
                            bundle.putString("projectName",intent.getStringExtra("projectName"))

                            val editBoardFragment = EditBoardFragment()
                            editBoardFragment.arguments = bundle
                            supportFragmentManager.beginTransaction()
                                .replace(binding.tasksView.id,editBoardFragment)
                                .addToBackStack("EditBoardFragment")
                                .commit()
                            
                        }
                        else{
                            Toast.makeText(this@TasksActivity, "Only The Board Creator Is Allowed To Edit", Toast.LENGTH_SHORT).show()
                        }

                        
                    }
                }
                true
            }
            title = intent.getStringExtra("projectName")
            inflateMenu(R.menu.menu_for_tasks)
            if(intent.getIntExtra("projectCreatedByID",0) != viewModel.getCurrentUserID()){

                menu.findItem(R.id.edit_board).isVisible = false
            }
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
            bundle.putString("projectName",intent.getStringExtra("projectName"))
            bundle.putInt("projectCreatedByID",intent.getIntExtra("projectCreatedByID",0))
            tasksOfProjectFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(binding.tasksView.id, tasksOfProjectFragment)
                .addToBackStack("TasksOfProjectFragment")
                .commit()
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

    override fun onBackPressed() {
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
            super.onBackPressed()
        }
    }



}


