package com.example.learn2

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.DatePicker.OnDateChangedListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.learn2.databinding.ActivityMainPageBinding
import com.google.firebase.auth.ActionCodeUrl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dataModel.ImageData
import dataModel.InfoModel

class MainPage : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMainPage.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main_page)
        if(FirebaseAuth.getInstance().currentUser!!.uid!=null){
            FirebaseDatabase.getInstance().getReference("Students").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .get().addOnSuccessListener {
                    if(it.exists()){
                        val data=it.getValue(InfoModel::class.java)
                        val images=data!!.image
                        navView.getHeaderView(0).findViewById<TextView>(R.id.name).setText(data!!.firstName.toString()+" "+data.lastName.toString())
                        Glide.with(this).load(images).into(navView.findViewById<ImageView>(R.id.imageView))
                    }
                }

            navView.getHeaderView(0).findViewById<TextView>(R.id.email).setText(FirebaseAuth.getInstance().currentUser!!.email)
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_page, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main_page)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}