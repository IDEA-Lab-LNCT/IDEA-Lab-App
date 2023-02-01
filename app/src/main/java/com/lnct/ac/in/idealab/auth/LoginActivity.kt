package com.lnct.ac.`in`.idealab.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.lnct.ac.`in`.idealab.R
import com.lnct.ac.`in`.idealab.activity.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(){
    private val TAG = "LoginActivity"
    private lateinit var otpVerificationDialog : OTPVerificationDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        
        findViewById<TextView>(R.id.btnSkip).setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                Toast.makeText(this@LoginActivity, "Welcome! to AICTE IDEA Lab", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                finish()
            }
        })


       btnLogin.setOnClickListener{

       }


    }




}