package com.jumrukovski.datastoreexample

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jumrukovski.datastoreexample.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.getAppPreferences()
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        mainViewModel.appPreferences.observe(this) { appPreferences ->
            with(binding) {
                input.setText(appPreferences.keyString)

                val view = when (appPreferences.keyBoolean) {
                    true -> radioYes
                    false -> radioNo
                }

                view.isChecked = true
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            button.setOnClickListener {
                hideKeyboard(input)
                mainViewModel.updateStringValue(input.text.toString())
            }
            radioYes.setOnClickListener { mainViewModel.updateBooleanValue(true) }
            radioNo.setOnClickListener { mainViewModel.updateBooleanValue(false) }
        }
    }

    private fun hideKeyboard(view: View) {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}