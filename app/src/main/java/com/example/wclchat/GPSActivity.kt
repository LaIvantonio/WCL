package com.example.wclchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wclchat.databinding.ActivityGpsactivityBinding
import com.example.wclchat.fragments.MainFragment
import com.example.wclchat.fragments.SettingsFragment
import com.example.wclchat.fragments.TracksFragment
import com.example.wclchat.utils.openFragment

class GPSActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGpsactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGpsactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBottomNavClicks()
        openFragment(MainFragment.newInstance())
    }

    private fun onBottomNavClicks() {
        binding.bNan.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.id_home -> openFragment(MainFragment.newInstance())
                R.id.id_tracks -> openFragment(TracksFragment.newInstance())
                R.id.id_settings -> openFragment(SettingsFragment())
            }
            true
        }
    }
}