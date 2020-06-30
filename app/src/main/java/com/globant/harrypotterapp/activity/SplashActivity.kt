package com.globant.harrypotterapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.globant.harrypotterapp.databinding.ActivitySplashBinding
import com.globant.harrypotterapp.viewmodel.SplashState
import com.globant.harrypotterapp.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {
    private val splashViewModel by viewModel<SplashViewModel>()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        splashViewModel.getSplashStateLiveData().observe(::getLifecycle, ::runAnimation)
        splashViewModel.startTimer()
    }

    private fun runAnimation(state: SplashState) {
        when (state) {
            SplashState.START -> binding.logo.performClick()
            SplashState.FINISH -> {
                startActivity(MainActivity.getIntent(this))
                finish()
            }
        }
    }
}
