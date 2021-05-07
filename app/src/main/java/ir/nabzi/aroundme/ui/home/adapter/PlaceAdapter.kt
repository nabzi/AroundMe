package ir.nabzi.aroundme.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.nabzi.aroundme.R
import ir.nabzi.aroundme.databinding.ItemPlaceLayoutBinding
import ir.nabzi.aroundme.model.Place

abstract class PlaceAdapter(val context: Context, val onItemClick: (String) -> Unit
) :
        ListAdapter<Place, RecyclerView.ViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder =
            PlaceViewHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            R.layout.item_place_layout, parent, false
                    )
            )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var item = getItem(position)
        if (item != null) {
            (holder as PlaceViewHolder).bindTo(item);
        }
        if ((position >= itemCount - 1))
            loadMore(item);
    }

    companion object {
        //This diff callback informs the PagedListAdapter how to compute list differences when new
        private val diffCallback = object : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean =
                    oldItem === newItem

            override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean =
                    oldItem.id == newItem.id
        }
    }

    abstract fun loadMore(lastItem : Place)

    inner class PlaceViewHolder(
            val binding: ItemPlaceLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        var aPlace: Place? = null
        fun bindTo(item: Place) {
            this.aPlace = item
            with(binding) {
                placeitem = item
                binding.cvPlace.setOnClickListener {
                    aPlace?.let {
                        onItemClick(it.id)
                    }
                }
                executePendingBindings()
            }
        }
    }
}
