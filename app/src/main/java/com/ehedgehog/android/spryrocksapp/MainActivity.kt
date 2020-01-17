package com.ehedgehog.android.spryrocksapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = this.findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

fun ImageView.setImageWithGlide(@DrawableRes @RawRes resourceId: Int, isRound: Boolean) {
    context?.let {
        val builder = Glide.with(it)
            .load(resourceId)
            .centerCrop()
            .fitCenter()

        if (isRound)
            builder.apply(RequestOptions.circleCropTransform())

        builder.into(this)
    }
}