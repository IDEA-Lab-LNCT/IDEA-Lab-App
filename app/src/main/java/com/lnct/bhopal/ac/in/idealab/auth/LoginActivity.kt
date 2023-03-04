package com.lnct.bhopal.ac.`in`.idealab.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lnct.ac.`in`.idealab.Models.User
import com.lnct.bhopal.ac.`in`.idealab.Utils
import com.lnct.bhopal.ac.`in`.idealab.activity.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit
import com.lnct.bhopal.ac.`in`.idealab.R


class LoginActivity : AppCompatActivity(){

    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth;
    private val TAG = "LoginActivity"
    private lateinit var otpVerificationDialog : OTPVerificationDialog
    private lateinit var phoneNumber : String
    private var varificationID : String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth



        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                otpVerificationDialog.dismiss()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                btnLogin.isEnabled = true
                loading.visibility = View.GONE

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent:$verificationId")

                varificationID = verificationId
                btnLogin.isEnabled = false

                otpVerificationDialog = OTPVerificationDialog(this@LoginActivity,phoneNumber,verificationId, ::signInWithPhoneAuthCredential)
                otpVerificationDialog.setCancelable(false)
                otpVerificationDialog.show()

            }
        }


        
       btnLogin.setOnClickListener{
           phoneNumber = evPhone.text.toString().trim()
           if(phoneNumber.length ==  10){
               btnLogin.isEnabled = false
               loading.visibility = View.VISIBLE
               Log.d(TAG,"Send OTP called")
               val options = PhoneAuthOptions.newBuilder(auth)
                   .setPhoneNumber("+91"+phoneNumber)       // Phone number to verify
                   .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                   .setActivity(this)                 // Activity (for callback binding)
                   .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                   .build()
               PhoneAuthProvider.verifyPhoneNumber(options)

           }else {
               Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show()
           }
           
       }















        findViewById<TextView>(R.id.btnSkip).setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                Toast.makeText(this@LoginActivity, "Welcome! to AICTE IDEA Lab", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()
            }
        })


    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show()
                    db.collection("users").document(phoneNumber).get().addOnCompleteListener(this)  { task ->
                        if (task.isSuccessful) {
                            val u = task.result.get("name")
                            Log.d(TAG,u.toString())
                            if(u == null){
                                val i = Intent(this, RegisterActivity::class.java)
                                i.putExtra("PHONE",phoneNumber)
                                startActivity(i)
                                finishAffinity()
                            }
                          else {
                                val user = task.result
                                val uName = user.get("name").toString()
                                val uEmail = user.get("email").toString()
                                val uBranch = user.get("branch").toString()
                                val uCollege = user.get("college").toString()
                                val uWhatsApp = user.get("whatsapp").toString()
                                val uDOB = user.get("dob").toString()
                                val uEnroll = user.get("enrollment").toString()
                                val uAddr = user.get("address").toString()
                                val uSem = user.get("semester").toString()
                                val github = user.get("github").toString()
                                val skills = user.get("skills")

                                val uId = user.id

                                Log.d(TAG,"USER Skills  :---> "+skills.toString())
                                val userObj = User(uName,uEmail,uBranch,uCollege,uWhatsApp,uId,uDOB,uEnroll,uAddr,uSem.toInt(),github,
                                    skills as ArrayList<String>
                                )
                                Utils.saveUser(this,userObj)

                                Toast.makeText(this, "Welcome! "+u.toString(), Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, HomeActivity::class.java))
                                finishAffinity()
                            }


                        } else {
                            Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()

                        }
                    }

                } else {
                    // Sign in failed, display a message and update the UI
                    btnLogin.isEnabled = true
                    loading.visibility = View.GONE
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        otpVerificationDialog = OTPVerificationDialog(this@LoginActivity,phoneNumber,varificationID, ::signInWithPhoneAuthCredential)
                        otpVerificationDialog.setCancelable(false)
                        otpVerificationDialog.show()
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }

}