package com.osmancancinar.moviesapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.osmancancinar.moviesapp.R
import com.osmancancinar.moviesapp.databinding.ActivityMainBinding
import com.osmancancinar.moviesapp.viewModels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.AppTheme)
        setContentView(binding.root)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.createNotificationChannel()
        viewModel.sendNotification(this)

        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this,navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }
}