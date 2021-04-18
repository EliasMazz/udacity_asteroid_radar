package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.MainActivity
import com.udacity.asteroidradar.MyApplication
import com.udacity.asteroidradar.network.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.common.dependencyinjection.CompositionRoot
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.network.PictureOfDay
import java.lang.reflect.Array.get

class MainFragment : Fragment() {

    private val compositionRoot get() = (requireActivity() as MainActivity).compositionRoot

    private val viewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            compositionRoot.asteroidService,
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

        viewModel.listAsteroid.observe(viewLifecycleOwner, Observer<List<Asteroid>> {
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


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
