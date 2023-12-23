package com.example.wclchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.wclchat.databinding.*
import org.osmdroid.util.*
import org.osmdroid.views.overlay.*
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class QuestDetailsFragment : Fragment() {

    private lateinit var binding: FragmentQuestDetailsBinding
    private lateinit var attraction: Attraction

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Получение объекта Attraction из аргументов
        arguments?.let {
            attraction = it.getSerializable("attraction") as Attraction
        }
        setupMap(attraction)
        setupQuestInfo(attraction)
        setupButtons()
    }

    private fun setupMap(attraction: Attraction) {
        // Настройка карты OSMdroid
        val mapController = binding.mapView.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(attraction.latitude, attraction.longitude)
        mapController.setCenter(startPoint)

        // Добавление маркера для точки назначения
        val destinationMarker = Marker(binding.mapView)
        destinationMarker.position = startPoint
        destinationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        binding.mapView.overlays.add(destinationMarker)
    }

    private fun setupQuestInfo(attraction: Attraction) {
        val textViewQuestInfo = view?.findViewById<TextView>(R.id.textViewQuestInfo)
        textViewQuestInfo?.text = getString(R.string.quest_info_format,
            attraction.name,
            calculateDistance(attraction), // Функция для расчета дистанции
            calculateTime(attraction) // Функция для расчета времени
        )
    }
    // Пример функции для расчета дистанции
    private fun calculateDistance(attraction: Attraction): String {
        // Реализуйте логику расчета дистанции до достопримечательности
        return "10 км"
    }
    // Пример функции для расчета времени
    private fun calculateTime(attraction: Attraction): String {
        // Реализуйте логику расчета времени до достопримечательности
        return "1 час 30 минут"
    }

    private fun setupButtons() {
        val buttonReturn = view?.findViewById<Button>(R.id.buttonReturn)
        val buttonStartQuest = view?.findViewById<Button>(R.id.buttonStartQuest)

        buttonReturn?.setOnClickListener {
            // Возврат к списку квестов
            requireActivity().supportFragmentManager.popBackStack()
        }

        buttonStartQuest?.setOnClickListener {
            // Начало квеста и навигации
            val attraction = attraction // Получите объект Attraction
            startNavigation(attraction)
        }
    }


    private fun startNavigation(attraction: Attraction) {
        // Запуск навигации, как описано ранее
        val gmmIntentUri = Uri.parse("google.navigation:q=${attraction.latitude},${attraction.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            // Обработка случая, когда Google Maps не установлен
            Toast.makeText(requireContext(), "Google Maps не установлен", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        fun newInstance(attraction: Attraction): QuestDetailsFragment {
            val fragment = QuestDetailsFragment()
            val args = Bundle()
            args.putSerializable("attraction", attraction)
            fragment.arguments = args
            return fragment
        }
    }
}
