package com.udacity.asteroidradar.features.main.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.features.main.ui.model.AsteroidFilterViewData
import com.udacity.asteroidradar.features.main.ui.model.AsteroidViewData
import com.udacity.asteroidradar.features.main.viewmodel.MainViewModel
import com.udacity.asteroidradar.features.main.viewmodel.MainViewModelFactory
import com.udacity.asteroidradar.features.main.domain.RefreshAsteroidListUseCase.Result

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val compositionRoot get() = (requireActivity() as MainActivity).compositionRoot

    private val viewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            filteredAsteroidListUseCase = compositionRoot.filteredAsteroidListUseCase,
            refreshAsteroidListUseCase = compositionRoot.refreshAsteroidListUseCase,
            pictureOfDayUsecase = compositionRoot.pictureOfDayUseCase
        )

        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        _binding = DataBindingUtil.inflate<FragmentMainBinding>(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )

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
                Result.Success -> showToast(getString(R.string.refresh_sucessfuly))
                Result.GeneralError -> showToast(getString(R.string.refresh_generic_error))
                Result.NoInternet -> showToast(getString(R.string.refresh_no_internet_connection))
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun showToast(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
