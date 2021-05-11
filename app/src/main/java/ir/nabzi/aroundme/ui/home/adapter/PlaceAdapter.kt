package ir.nabzi.aroundme.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.nabzi.aroundme.R
import ir.nabzi.aroundme.databinding.ItemPlaceLayoutBinding
import ir.nabzi.aroundme.model.Place

abstract class PlaceAdapter( val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<Place>? = null
    var isLoading = false
    var isMoreDataAvailable = true //todo
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder =
            PlaceViewHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            R.layout.item_place_layout, parent, false
                    )
            )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var item = list?.get(position)
        if (item != null) {
            (holder as PlaceViewHolder).bindTo(item);
        }
        if ( position >= itemCount - 1 && isMoreDataAvailable && !isLoading) {
            isLoading = true;
            loadMore(item);
        }
    }
    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    abstract fun loadMore(lastItem : Place?)
    fun notifyDataChanged() {
        notifyDataSetChanged()
        isLoading = false
    }
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
