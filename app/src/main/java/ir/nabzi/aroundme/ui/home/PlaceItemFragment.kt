package ir.nabzi.aroundme.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.nabzi.aroundme.data.model.Place

typealias CALLBACK = (String)->Unit

class PlaceItemFragment : Fragment() {
    var clickListener :  CALLBACK? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val placeView = PlaceView(layoutInflater, container, clickListener)
        requireArguments().getParcelable<Place>(KEY_PLACE)?.let{
            placeView.bind(it)
        }
        return placeView.binding.root
    }

    companion object {
        const val KEY_PLACE = "key_place"
        fun create(place: Place, listener: CALLBACK): PlaceItemFragment {
            return PlaceItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_PLACE, place)
                }
                clickListener = listener
            }
        }
    }
}