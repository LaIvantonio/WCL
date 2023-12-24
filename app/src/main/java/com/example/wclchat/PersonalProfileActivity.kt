package com.example.wclchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.wclchat.databinding.ActivityPersonalProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class PersonalProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalProfileBinding
    private lateinit var viewModel: PersonalProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_profile)

        viewModel = ViewModelProvider(this).get(PersonalProfileViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Инициализация данных пользователя
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val userName = it.displayName ?: "Пользователь"
            viewModel.userName.value = "Имя: $userName"
        }
    }
}