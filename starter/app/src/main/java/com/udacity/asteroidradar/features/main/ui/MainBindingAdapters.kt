package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.data.network.models.PictureOfDayResponse
import com.udacity.asteroidradar.features.main.ui.model.PictureOfDayViewData

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("imageUrl")
fun bindImageOfDay(view: ImageView, pictureOfDayViewData: PictureOfDayViewData?) {
    pictureOfDayViewData?.url.let {
        Picasso.with(view.context).load(it)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .into(view)
    }
}
