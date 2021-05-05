package ir.nabzi.aroundme.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.nabzi.aroundme.R
import ir.nabzi.aroundme.databinding.FragmentPlaceDetailsBinding
import ir.nabzi.aroundme.ui.home.PlaceViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel


class PlaceDetailsFragment : Fragment() {

    private val vmodel: PlaceViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPlaceDetailsBinding.inflate(inflater, container, false)
        binding.vmodel = vmodel
        binding.lifecycleOwner = this
        return binding.root
    }

}