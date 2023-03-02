package com.example.weatherforecast.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FavoriteRowBinding
import com.example.weatherforecast.databinding.FragmentFavoritesBinding
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.ui.dashboard.SettingsFragmentDirections
import com.example.weatherforecast.ui.home.HomeViewModel


class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    favoritesViewModel=ViewModelProvider(this,FavoritesViewModelFactory(requireContext())).get(FavoritesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        favoritesViewModel=ViewModelProvider(this,FavoritesViewModelFactory(requireContext())).get(FavoritesViewModel::class.java)
        binding.btnAddToFavorites.setOnClickListener{
            val action=
                FavoritesFragmentDirections.actionFavoritesFragmentToMapsFragment(false,true)
            Navigation.findNavController(it).navigate(action)
        }
        return root
    }

}