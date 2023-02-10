package com.lnct.bhopal.ac.`in`.idealab.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.jesusd0897.gallerydroid.model.GalleryDroid
import com.jesusd0897.gallerydroid.model.Picture
import com.jesusd0897.gallerydroid.view.fragment.GalleryFragment
import com.jesusd0897.gallerydroid.view.fragment.OnPictureItemClickListener
import com.jesusd0897.gallerydroid.view.fragment.PicturePagerFragment
import com.lnct.bhopal.ac.`in`.idealab.R
import com.lnct.bhopal.ac.`in`.idealab.Utils
import com.lnct.bhopal.ac.`in`.idealab.auth.LoginActivity
import com.lnct.bhopal.ac.`in`.idealab.quiz.QuizWelcomeFragment
import com.lnct.bhopal.ac.`in`.idealab.frgments.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var fm: FragmentManager
    private lateinit var linkeden_link: ImageView
    private lateinit var instagram_link: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()



        drawerLayout = findViewById(R.id.nav_drawer)
        navigationView = findViewById(R.id.navigation_view)
        navigationView.itemIconTintList = null

        toolbar = findViewById(R.id.tool_bar)


        linkeden_link = findViewById(R.id.linkedin_link)
        instagram_link = findViewById(R.id.instagram_link)

        button_logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

       setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open_Nav,R.string.Close_Nav)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
        loadFragment(HomeFragment(),"Home")

        navigationView.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val id = item.itemId

                when(id){
                    R.id.home -> loadFragment(HomeFragment(),"Home")
                    R.id.event -> loadFragment(Event(),"Events")
                    R.id.about -> loadFragment(AboutUs(),"About Us")
                    R.id.contact -> loadFragment(ContactUs(),"Contact Us")
                    R.id.highlights -> launchHighlights()
                    R.id.projects -> loadFragment(ProjectFragment(),"Projects")
                    else -> {}

                }

      //  var transaction = getSupportFragmentManager().beginTransaction()
      //  transaction.replace(R.id.frame_layout, ContactUs())
      //  transaction.commit()

                drawerLayout.closeDrawer(GravityCompat.START)
                return true

            }
        })

        if(Utils.isUserPresent(this)){
            val header: View = navigationView.getHeaderView(0)
            val userNameTextView = header.findViewById<TextView>(R.id.user_name_nav)
            userNameTextView.text = Utils.getUser(this).name
        }


        linkeden_link.setOnClickListener(View.OnClickListener { object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var i : Intent

            }
        }

        })

    }

    fun loadFragment(fragment: Fragment, fragName: String) {
        toolbar.title = fragName
        val fm : FragmentManager = supportFragmentManager;
//        fm = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()

        val currentFragment: Fragment? = getSupportFragmentManager().findFragmentById(R.id.container)


        if (currentFragment != null) {
            ft.remove(currentFragment)
        }
        ft.add(R.id.container,fragment)
        ft.commit()
    }


    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    fun launchHighlights(){
        toolbar.title = "Highlights"
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) //Optional
            .replace(R.id.container, GalleryFragment.newInstance())
            .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is GalleryFragment) {
            fragment.injectGallery(
                GalleryDroid(
                    listOf(
                        Picture(
                            fileURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/g1",
                            fileThumbURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/g1",
                        ),
                        Picture(
                            fileURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/g2",
                            fileThumbURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/g2",
                        ),
                        Picture(
                            fileURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h1",
                            fileThumbURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h1",
                        ),
                        Picture(
                            fileURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h2",
                            fileThumbURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h2",
                        ),Picture(
                            fileURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h3",
                            fileThumbURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h3",
                        ),Picture(
                            fileURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h4",
                            fileThumbURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h4",
                        ),
                        Picture(
                            fileURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h5",
                            fileThumbURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h5",
                        ),
                        Picture(
                            fileURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h6",
                            fileThumbURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h6",
                        )
                    )
                )
                    .layoutManager(GalleryDroid.LAYOUT_STAGGERED_GRID)
                    .pictureCornerRadius(16f)
                    .pictureElevation(8f)
                    .transformer(GalleryDroid.TRANSFORMER_CUBE_OUT)
                    .spacing(12)
                    .portraitColumns(1)
                    .landscapeColumns(1)
                    .autoClickHandler(true)
                    .useLabels(false)
            )
        }


    }

}