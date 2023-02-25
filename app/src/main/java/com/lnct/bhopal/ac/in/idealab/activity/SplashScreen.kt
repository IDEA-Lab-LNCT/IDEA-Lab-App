package com.lnct.bhopal.ac.`in`.idealab.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lnct.bhopal.ac.`in`.idealab.R
import com.lnct.bhopal.ac.`in`.idealab.Utils
import com.lnct.bhopal.ac.`in`.idealab.auth.LoginActivity

class SplashScreen : AppCompatActivity() {
    private lateinit var logoImg : ImageView
    private lateinit var tv2: TextView
    private lateinit var  icon_holder: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        logoImg = findViewById(R.id.launch_image)
        tv2 = findViewById(R.id.tv2)
        icon_holder = findViewById(R.id.icon_holder)

        Handler().postDelayed({
            if(!Utils.isUserPresent(this)){
                finish()
                startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            }
            else {
                finish()
                startActivity(Intent(this@SplashScreen, HomeActivity::class.java))}

        },2100

        )

        val anim = AnimationUtils.loadAnimation(this, R.anim.fade_scale)
        icon_holder.startAnimation(anim)
        logoImg.startAnimation(anim)
        tv2.startAnimation(anim)

        Utils.createImageCacheDir(this)

    }
}