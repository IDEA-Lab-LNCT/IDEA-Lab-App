package com.lnct.bhopal.ac.`in`.idealab.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lnct.ac.`in`.idealab.Models.User
import com.lnct.bhopal.ac.`in`.idealab.R
import com.lnct.bhopal.ac.`in`.idealab.Utils
import com.lnct.bhopal.ac.`in`.idealab.activity.HomeActivity
import kotlinx.android.synthetic.main.activity_register.*
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

        evDOB.addTextChangedListener(tw)


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
                val dob = evDOB.text.toString().trim()
                val enrollment = evEnrollment.text.toString().trim()
                val address = evAddress.text.toString().trim()
                val sem = evSem.text.toString().trim()


                if(name.length != 0
                    && email.length != 0
                    && branch.length != 0
                    && college.length != 0
                    && phone.length == 10
                    && dob.length != 0
                    && enrollment.length != 0
                    && address.length != 0
                    && sem.length != 0)
                    enableBtn = true

                 val skills = kotlin.collections.ArrayList<String>()

                Log.d(TAG,name + " "+email + " "+branch + " "+college + " "+phone + " "+dob+" "+enrollment+" "+address)

                if(enableBtn){
                    val user = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "branch" to branch,
                        "college" to college,
                        "whatsapp" to phone,
                        "dob" to dob,
                        "enrollment" to enrollment,
                        "address" to address,
                        "semester" to sem.toInt(),
                        "github" to "Link not set",
                        "skills" to skills
                    )
                    loading.visibility = View.VISIBLE
                    btnRegister.isEnabled = false

                    db.collection("users").document(intent.getStringExtra("PHONE")!!)
                        .set(user)
                        .addOnSuccessListener {
                            loading.visibility = View.GONE
                            val userObj = User(name,email,branch,college,phone,intent.getStringExtra("PHONE")!!,dob,enrollment,address,sem.toInt(),"Link not set",skills)
                            Utils.saveUser(this@RegisterActivity,userObj)

                            Toast.makeText(this@RegisterActivity, "Registration Complete", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                            finishAffinity()
                        }
                        .addOnFailureListener {

                                e -> Log.w(TAG, "Server Error", e)

                            loading.visibility = View.GONE
                            btnRegister.isEnabled = true
                            Toast.makeText(this@RegisterActivity, "Server Error", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finishAffinity()}


                }else {
                    Toast.makeText(this@RegisterActivity, "Invaild Credentials",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    var tw: TextWatcher = object : TextWatcher {
        private var current = ""
        private val ddmmyyyy = "DDMMYYYY"
        private val cal: Calendar = Calendar.getInstance()

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.toString() != current) {
                var clean = s.toString().replace("[^\\d.]|\\.".toRegex(), "")
                val cleanC = current.replace("[^\\d.]|\\.".toRegex(), "")
                val cl = clean.length
                var sel = cl
                var i = 2
                while (i <= cl && i < 6) {
                    sel++
                    i += 2
                }
                //Fix for pressing delete next to a forward slash
                if (clean == cleanC) sel--
                if (clean.length < 8) {
                    clean = clean + ddmmyyyy.substring(clean.length)
                } else {
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    var day = clean.substring(0, 2).toInt()
                    var mon = clean.substring(2, 4).toInt()
                    var year = clean.substring(4, 8).toInt()
                    mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                    cal.set(Calendar.MONTH, mon - 1)
                    year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                    cal.set(Calendar.YEAR, year)
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012
                    day =
                        if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
                    clean = String.format("%02d%02d%02d", day, mon, year)
                }
                clean = String.format(
                    "%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8)
                )
                sel = if (sel < 0) 0 else sel
                current = clean
                evDOB.setText(current)
                evDOB.setSelection(if (sel < current.length) sel else current.length)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

}