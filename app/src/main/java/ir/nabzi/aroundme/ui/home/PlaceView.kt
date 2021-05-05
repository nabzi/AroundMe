package ir.nabzi.aroundme.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import ir.nabzi.aroundme.R
import ir.nabzi.aroundme.databinding.ItemPlaceLayoutBindingImpl
import ir.nabzi.aroundme.model.Place

class PlaceView(layoutInflater: LayoutInflater, container: ViewGroup?, val onClick: CALLBACK?) {
    val binding : ItemPlaceLayoutBindingImpl = DataBindingUtil.inflate(
        layoutInflater,
        R.layout.item_place_layout, container,false
    )
    fun bind(place: Place) {
        with(binding) {
            placeitem = place
            cvPlace.setOnClickListener {
                onClick?.invoke(place.id)
            }
            executePendingBindings()
        }

    }
}

