package com.jumrukovski.datastoreexample

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jumrukovski.datastoreexample.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.appPreferences.collectLatest { appPreferences ->
                    with(binding) {
                        input.setText(appPreferences.name)

                        val view = when (appPreferences.isStudent) {
                            true -> radioYes
                            false -> radioNo
                        }

                        view.isChecked = true
                    }
                }
            }
        }

        setupClickListeners()
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