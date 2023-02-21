package com.lnct.bhopal.ac.`in`.idealab.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jesusd0897.gallerydroid.model.GalleryDroid
import com.jesusd0897.gallerydroid.model.Picture
import com.jesusd0897.gallerydroid.view.fragment.GalleryFragment
import com.lnct.bhopal.ac.`in`.idealab.Constants.idealab_instagram
import com.lnct.bhopal.ac.`in`.idealab.Constants.idealab_website
import com.lnct.bhopal.ac.`in`.idealab.R
import com.lnct.bhopal.ac.`in`.idealab.Utils
import com.lnct.bhopal.ac.`in`.idealab.auth.LoginActivity
import com.lnct.bhopal.ac.`in`.idealab.frgments.EventDirections
import com.lnct.bhopal.ac.`in`.idealab.frgments.HomeFragmentDirections
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header.view.*

private lateinit var navController: NavController
class HomeActivity : AppCompatActivity() {

    private val TAG = "HOME"
    val db = Firebase.firestore

    private lateinit var auth: FirebaseAuth;
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var pics : ArrayList<Picture>


    private val navController by lazy {
        Navigation.findNavController(this, R.id.navHostFragment)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        pics = getPics()


        auth = FirebaseAuth.getInstance()
        drawerLayout = findViewById(R.id.nav_drawer)
        navigationView = findViewById(R.id.navigation_view)
        navigationView.itemIconTintList = null
        toolbar = findViewById(R.id.tool_bar)

        setSupportActionBar(toolbar)
        setupDrawer()

        val header = navigationView.getHeaderView(0)

        button_logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

        if(Utils.isUserPresent(this))
        header.user_name_nav.text = Utils.getUser(this).name
        else header.user_name_nav.text = "Username"

        header.view_profile_button.setOnClickListener {
            if(Utils.isUserPresent(this)){

                navController.navigate(R.id.profileFragment)

                drawerLayout.closeDrawer(GravityCompat.START)

            }else {
                startActivity(Intent(this,LoginActivity::class.java))
                Toast.makeText(this, "Please login to view profile.", Toast.LENGTH_LONG).show()
                finishAffinity()
            }

        }

        instagram_link.setOnClickListener {
            val uri = Uri.parse(idealab_instagram)
            val likeIng = Intent(Intent.ACTION_VIEW, uri)

            likeIng.setPackage("com.instagram.android")

            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com")
                    )
                )
            }
        }

        website_link.setOnClickListener {
            val viewIntent = Intent(
                "android.intent.action.VIEW",
                Uri.parse(idealab_website)
            )
            startActivity(viewIntent)
        }



    }

    private fun setupDrawer() {
        navigationView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onAttachFragment(fragment: Fragment) {
            Log.d(TAG,"ON ATTACH CALLED")
            super.onAttachFragment(fragment)
            if (fragment is GalleryFragment) {
                fragment.injectGallery(
                    GalleryDroid(
                      pics
                    )
                        .layoutManager(GalleryDroid.LAYOUT_STAGGERED_GRID)
                        .pictureCornerRadius(16f)
                        .pictureElevation(8f)
                        .transformer(GalleryDroid.TRANSFORMER_CUBE_OUT)
                        .spacing(12)
                        .portraitColumns(2)
                        .landscapeColumns(3)
                        .autoClickHandler(true)
                        .useLabels(false)
                )
            }

    }

    fun getPics() : ArrayList<Picture> {
        val pics = ArrayList<Picture>()
        db.collection("highlights").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                       val url = document.get("url").toString()
                        Log.d(TAG,"IMAG URL ->" + url)
                        val pic = Picture(
                            fileURL = url,
                            fileThumbURL = url
                        )
                        pics.add(pic)


                    }
                }
            }

        Log.d(TAG,"PICS END")
        return  pics
    }

}