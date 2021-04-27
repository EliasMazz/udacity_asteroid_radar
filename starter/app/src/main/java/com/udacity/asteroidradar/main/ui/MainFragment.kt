package com.udacity.asteroidradar.main.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.MainActivity
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.model.AsteroidFilterViewData
import com.udacity.asteroidradar.main.model.AsteroidViewData
import com.udacity.asteroidradar.main.viewmodel.MainViewModel
import com.udacity.asteroidradar.main.viewmodel.MainViewModelFactory
import com.udacity.asteroidradar.main.repository.AsteroidRepository

class MainFragment : Fragment() {

    private val compositionRoot get() = (requireActivity() as MainActivity).compositionRoot

    private val viewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            compositionRoot.asteroidRepository,
            compositionRoot.pictureOfDayService
        )

        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = AsteroidAdapter(AsteroidAdapter.AsteroidClickListener { selectedAsteroid ->
            viewModel.onAsteroidClicked(selectedAsteroid)
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer<List<AsteroidViewData>> {
            adapter.submitList(it)
        })

        viewModel.navigateToAsteroidDetail.observe(viewLifecycleOwner, Observer { selectedAsteroid ->
            selectedAsteroid?.let {
                this.findNavController().navigate(
                    MainFragmentDirections.actionShowDetail(selectedAsteroid)
                )
                viewModel.onAsteroidNavigated()
            }
        })

        viewModel.refreshAsteroidResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                AsteroidRepository.Result.Success -> Toast.makeText(requireContext(), "Asteroids was refreshed sucessfuly", Toast.LENGTH_SHORT).show()
                AsteroidRepository.Result.GeneralError -> Toast.makeText(requireContext(), "Error while trying to refresh asteroids", Toast.LENGTH_SHORT).show()
                AsteroidRepository.Result.NoInternet -> Toast.makeText(requireContext(), "Error while trying to refresh, no internet connection", Toast.LENGTH_SHORT).show()
            }

        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_saved_menu -> viewModel.filterAsteroidList(AsteroidFilterViewData.SAVED)
            R.id.show_today_menu -> viewModel.filterAsteroidList(AsteroidFilterViewData.TODAY)
            R.id.show_week_menu -> viewModel.filterAsteroidList(AsteroidFilterViewData.WEEK)
        }
        return super.onOptionsItemSelected(item)
    }
}
