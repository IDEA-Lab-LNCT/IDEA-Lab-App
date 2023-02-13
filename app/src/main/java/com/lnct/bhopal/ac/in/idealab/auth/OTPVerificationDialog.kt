package com.lnct.bhopal.ac.`in`.idealab.auth

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.lnct.bhopal.ac.`in`.idealab.R
import kotlinx.android.synthetic.main.otp_verification_layout.*

class OTPVerificationDialog(context : Context,var userPhone : String,var verificationId : String, val  signInWithPhoneAuthCredential : (PhoneAuthCredential) -> Unit ) : Dialog(context) {

   private lateinit var otpET1 : EditText
    private lateinit var otpET2 : EditText
    private lateinit var otpET3 : EditText
    private lateinit var otpET4 : EditText
    private lateinit var otpET5 : EditText
    private lateinit var otpET6 : EditText
    private lateinit var resendBtn : TextView
    private lateinit var verifyBtn : Button

    private var resendTime : Long = 60 // Resend OTP time
    private var resendEnabled = false
    private var selectedETPosition = 0
    private val TAG = "OTPVerificationDialog"





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        window?.setBackgroundDrawable(ColorDrawable(context.resources.getColor(android.R.color.transparent)))
        setContentView(R.layout.otp_verification_layout)

        otpET1 = findViewById(R.id.otpET1)
        otpET2 = findViewById(R.id.otpET2)
        otpET3 = findViewById(R.id.otpET3)
        otpET4 = findViewById(R.id.otpET4)
        otpET5 = findViewById(R.id.otpET5)
        otpET6 = findViewById(R.id.otpET6)

        resendBtn = findViewById(R.id.TVresend)
        verifyBtn = findViewById(R.id.verifyButton)

        otpET1.addTextChangedListener(textWatcher)
        otpET2.addTextChangedListener(textWatcher)
        otpET3.addTextChangedListener(textWatcher)
        otpET4.addTextChangedListener(textWatcher)
        otpET5.addTextChangedListener(textWatcher)
        otpET6.addTextChangedListener(textWatcher)


        //By default open keyboard on first EditText
        showKeyboard(otpET1)

        // start countDown timer
        startCountDownTimer()

        // set email to textView
        tvPhone.setText("+91 "+userPhone)



        resendBtn.setOnClickListener{

        }

        verifyBtn.setOnClickListener{
            val otpCode = otpET1.text.toString()+otpET2.text.toString()+otpET3.text.toString()+otpET4.text.toString()+otpET5.text.toString()+otpET6.text.toString()
            if(otpCode.length == 6){
                Log.d("OTP",otpCode)
                if(verificationId != null){
                    verifyBtn.isEnabled = false
                    this.dismiss()
                    val credential = PhoneAuthProvider.getCredential(verificationId!!, otpCode)
                    signInWithPhoneAuthCredential(credential)
                }
            }else {
                verifyBtn.isEnabled = true
                Toast.makeText(context, "Enter complete 6-digit OTP", Toast.LENGTH_SHORT).show()
            }


        }





    }



    private val  textWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
           if(p0?.length!! == 1){

               if (selectedETPosition == 0){
                   // Select next edit text
                   selectedETPosition = 1
                   showKeyboard(otpET2)
               }else if (selectedETPosition == 1){
                   selectedETPosition = 2
                   showKeyboard(otpET3)

               }else if (selectedETPosition == 2){
                   selectedETPosition = 3
                   showKeyboard(otpET4)

               }
               else if (selectedETPosition == 3){
                   selectedETPosition = 4
                   showKeyboard(otpET5)

               }
               else if (selectedETPosition == 4){
                   selectedETPosition = 5
                   showKeyboard(otpET6)

               }else {
                   verifyBtn.setBackgroundColor(R.drawable.round_back_red)
               }
           } else p0?.clear()
        }
    }

    private fun showKeyboard(optET : EditText){
        optET.requestFocus()
        val inputMethodManager : InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.showSoftInput(optET, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun startCountDownTimer(){
         resendEnabled = false
        resendBtn.setTextColor(Color.parseColor("#99000000"))

       object : CountDownTimer(resendTime * 1000,1000){
           override fun onTick(millisUntilFinished: Long) {
               resendBtn.setText("Resend Code ("+(millisUntilFinished / 1000)+")")
           }

           override fun onFinish() {
               resendEnabled = true
               resendBtn.setText("Resend Code")
               resendBtn.setTextColor(context.resources.getColor(android.R.color.holo_blue_dark))
           }

       }.start()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if(keyCode == KeyEvent.KEYCODE_DEL){
             if (selectedETPosition == 5){
                selectedETPosition = 4
                showKeyboard(otpET5)
            }
            else if (selectedETPosition == 4){
                selectedETPosition = 3
                showKeyboard(otpET4)
            }
            else if(selectedETPosition == 3){
                // move to previous edit text
                selectedETPosition = 2
                showKeyboard(otpET3)
            }
            else if (selectedETPosition == 2){
                selectedETPosition = 1
                showKeyboard(otpET2)

            }else if (selectedETPosition == 1){
                selectedETPosition = 0
                showKeyboard(otpET1)
            }


            verifyBtn.setBackgroundResource(R.drawable.round_back_brown)
            return true
        }
        else {
            return super.onKeyUp(keyCode, event)
        }

    }


}