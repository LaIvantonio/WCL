package com.example.wclchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wclchat.databinding.FragmentQuestsBinding // Импорт для биндинга

class QuestsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var questsAdapter: QuestsAdapter
    private var _binding: FragmentQuestsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setupRecyclerView()
        observeAttractions()
    }

    private fun setupRecyclerView() {
        questsAdapter = QuestsAdapter { attraction ->
            val questDetailsFragment = QuestDetailsFragment.newInstance(attraction)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, questDetailsFragment) // Используйте правильный ID контейнера
                .addToBackStack(null)
                .commit()
        }
        // Установка LayoutManager для RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = questsAdapter
    }

    private fun observeAttractions() {
        viewModel.attractions.observe(viewLifecycleOwner, Observer { attractionsList ->
            // Обновление адаптера с новым списком квестов
            questsAdapter.submitList(attractionsList)
        })
    }

    private fun startNavigation(attraction: Attraction) {
        // Здесь реализуйте логику начала навигации
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Очистка ссылки на биндинг
    }

    companion object {
        // Фабричный метод для создания экземпляра фрагмента
    }
}
