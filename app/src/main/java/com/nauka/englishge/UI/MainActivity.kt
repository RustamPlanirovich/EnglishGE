package com.nauka.englishge.UI

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.nauka.englishge.R
import com.nauka.englishge.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.FileReader
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val navController = this.findNavController(R.id.activity_main_nav_host_fragment)
        val navView: BottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view)
        navView.setupWithNavController(navController)

    }
}