package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.data.network.models.PictureOfDayResponse

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("imageUrl")
fun bindImageOfDay(view: ImageView, pictureOfDayResponse: PictureOfDayResponse?) {
    if (pictureOfDayResponse?.mediaType == "image") {
        Picasso.with(view.context).load(pictureOfDayResponse.url)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .into(view)
    }
}
