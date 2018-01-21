package com.kotlin.loginregister

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by User on 12/9/2017.
 */

val glideOption = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.ic_place_holder)
        .error(R.drawable.ic_place_holder)

fun ImageView.loadImageFromURL(imgUrl: String?) {
    Glide.with(this.context).load(imgUrl).apply(glideOption).into(this)
}