package com.example.wclchat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.wclchat.db.MainDb
import com.example.wclchat.db.TrackItem
import com.example.wclchat.location.LocationModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class MainViewModel(db: MainDb) : ViewModel() {
    val dao = db.getDao()
    val locationUpdates = MutableLiveData<LocationModel>()
    val currentTrack = MutableLiveData<TrackItem>()
    val timeData = MutableLiveData<String>()
    val tracks = dao.getAllTracks().asLiveData()

    fun insertTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.insertTrack(trackItem)
    }

    fun deleteTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.deleteTrack(trackItem)
    }

    fun savePreferences(preferences: Preferences) = viewModelScope.launch {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val databaseReference = Firebase.database.getReference("usersPreferences")
            databaseReference.child(it).setValue(preferences)
        }
    }

    class ViewModelFactory(private val db: MainDb) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                return MainViewModel(db) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    fun loadCurrentWeather() {
        val weatherRepository = WeatherRepository()
        weatherRepository.getCurrentWeather("Rostov-on-Don", "e21e043d5b5c1cd57ab00cf3f656a7bb") { weatherResponse ->
            if (weatherResponse != null) {
                // Обновите UI или логику приложения с новыми данными о погоде
            } else {
                // Обработка ошибки
            }
        }
    }
}

