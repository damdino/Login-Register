package com.kotlin.loginregister

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    val imageUrl = "https://media1.tenor.com/images/f561eb5bf6ae4d3e05ed5dba5e44bbb5/tenor.gif?itemid=4845912"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        civ_profile.loadImageFromURL(imageUrl)

        intent?.let {
            val fullName = intent.extras.getString("full_name")
            val email = intent.extras.getString("email")

            tv_full_name.text = fullName
            tv_email.text = email
        }
    }
}