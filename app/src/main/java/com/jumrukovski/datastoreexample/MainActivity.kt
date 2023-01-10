package com.jumrukovski.datastoreexample

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jumrukovski.datastoreexample.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesManager = PreferencesManager(this@MainActivity)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            preferencesManager.preferencesFlow.collect { preferences ->
                with(binding) {
                    input.setText(preferences.keyString)
                    when (preferences.keyBoolean) {
                        true -> radioYes
                        false -> radioNo
                    }.apply {
                        isChecked = true
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            button.setOnClickListener {
                hideKeyboard(input)
                lifecycleScope.launch(Dispatchers.IO) {
                    preferencesManager.updateStringValue(input.text.toString().trim())
                }
            }
            radioYes.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    preferencesManager.updateBooleanValue(true)
                }
            }
            radioNo.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    preferencesManager.updateBooleanValue(false)
                }
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}