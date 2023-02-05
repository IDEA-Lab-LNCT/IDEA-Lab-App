package com.lnct.ac.`in`.idealab.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.NetworkResponse
import com.android.volley.VolleyError
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lnct.ac.`in`.idealab.Constants
import com.lnct.ac.`in`.idealab.Models.User
import com.lnct.ac.`in`.idealab.R
import com.lnct.ac.`in`.idealab.Utils
import com.lnct.ac.`in`.idealab.VolleyRequest
import com.lnct.ac.`in`.idealab.activity.HomeActivity
import com.lnct.ac.`in`.idealab.interfaces.CallBack
import com.lnct.ac.`in`.idealab.`interfaces`.login_finish
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class RegisterActivity : AppCompatActivity(){
    private val db = Firebase.firestore
    private lateinit var userName : TextInputEditText
    private lateinit var userEmail : TextInputEditText
    private lateinit var userPhone : TextInputEditText
    private lateinit var collegeDropDown : AutoCompleteTextView
    private lateinit var branchDropDown : AutoCompleteTextView
    private lateinit var loading : LinearLayout
    val TAG = "RegisterActivity"
    var enableBtn = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val phone = intent.getStringExtra("PHONE")

        collegeDropDown = findViewById(R.id.college_dropdown_menu)
        branchDropDown = findViewById(R.id.branch_dropdown_menu)
        userName = findViewById(R.id.evName)
        userEmail = findViewById(R.id.evEmail)
        userPhone = findViewById(R.id.evPhNo)
        loading = findViewById(R.id.loading)


        val collegeList = listOf("LNCT", "LNCTE", "LNCTS", "LNCTU","Other")
        val collegeDropDownAdapter = ArrayAdapter(this, R.layout.drop_down_list_item, collegeList)
        collegeDropDown?.setAdapter(collegeDropDownAdapter)

        val branchList = listOf("CSE","CY","IOT","AI/ML","Block Chain","DS","IT","EC","EX","EE","ME","CE","CM","Other")
        val branchDropDownAdapter = ArrayAdapter(this,R.layout.drop_down_list_item, branchList)
        branchDropDown?.setAdapter(branchDropDownAdapter)


        findViewById<Button>(R.id.btnRegister).setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                enableBtn = false;
                val name = userName.text.toString().trim()
                val email = userEmail.text.toString().trim()
                val branch = branchDropDown.text.toString().trim()
                val college = collegeDropDown.text.toString().trim()
                val phone = userPhone.text.toString().trim()

                if(name.length != 0 && email.length != 0 && branch.length != 0 && college.length != 0 && phone.length == 10)
                    enableBtn = true

                Log.d(TAG,name + " "+email + " "+branch + " "+college + " "+phone + " ")

                if(enableBtn){
                    val user = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "branch" to branch,
                        "college" to college,
                        "whatsapp" to phone
                    )

                    db.collection("users").document(phone)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this@RegisterActivity, "Registration Complete 👍", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity,HomeActivity::class.java))
                            finishAffinity()
                        }
                        .addOnFailureListener {
                                e -> Log.w(TAG, "Server Error", e)
                            Toast.makeText(this@RegisterActivity, "Server Error", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
                            finishAffinity()}


                }else {
                    Toast.makeText(this@RegisterActivity, "Invaild Credentials",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}