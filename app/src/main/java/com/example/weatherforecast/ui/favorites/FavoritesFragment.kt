package com.example.weatherforecast.ui.favorites

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.FragmentFavoritesBinding
import com.example.weatherforecast.model.Pojos.LocalDataState
import com.example.weatherforecast.model.Utils
import com.example.weatherforecast.ui.dashboard.SettingsFragmentDirections
import kotlinx.coroutines.launch

private const val TAG = "FavoritesFragment"
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritesViewModel: FavoritesViewModel
    lateinit var progressDialog: ProgressDialog
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
        favoritesViewModel.getFavoriteWeathers()
        if (Utils.isOnline(requireContext()))
        {
            binding.btnAddToFavorites.visibility=View.VISIBLE

        }
        else
        {
            binding.btnAddToFavorites.isEnabled=false
            binding.btnAddToFavorites.visibility=View.GONE

        }
        binding.btnAddToFavorites.setOnClickListener{
            val action=
                FavoritesFragmentDirections.actionFavoritesFragmentToMapsFragment(false,true,false)
            Navigation.findNavController(it).navigate(action)
        }
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(false) // blocks UI interaction

        lifecycleScope.launch{

            favoritesViewModel.welcomeFavoriteWeather.collect{
                when(it)
                {
                    is LocalDataState.Loading->{
                          progressDialog.show()
                    }
                    is LocalDataState.Fail->{
                       progressDialog.hide()
                        Toast.makeText(requireContext(),"Failed", Toast.LENGTH_SHORT).show()
                    }
                    is LocalDataState.Success->{
                        progressDialog.hide()
                        if(it.data?.isEmpty()==true)
                        {
                            binding.rvFavorites.visibility=View.GONE
                            binding.noFavHolerAnimation.visibility=View.VISIBLE
                        }
                        else
                        {
                            binding.rvFavorites.visibility=View.VISIBLE
                            binding.noFavHolerAnimation.visibility=View.GONE

                            binding.rvFavorites.apply {
                                adapter = FavoritesAdatpter(it.data,requireContext(),{
                                    favoritesViewModel.deleteFavorite(it)
                                },{
                                    Navigation.findNavController(requireView()).navigate(
                                        FavoritesFragmentDirections.actionFavoritesFragmentToNavigationHome().apply {
                                          isComingFav=true
                                            welcomFavObj=it
                                        })
                                   })
                                layoutManager = LinearLayoutManager(requireContext())
                                    .apply { orientation = RecyclerView.VERTICAL }
                            }
                        }

                    }
                }
            }

        }

        return root
    }

}