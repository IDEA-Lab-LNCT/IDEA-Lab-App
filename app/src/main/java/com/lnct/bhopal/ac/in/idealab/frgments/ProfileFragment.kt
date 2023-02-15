package com.lnct.bhopal.ac.`in`.idealab.frgments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lnct.ac.`in`.idealab.Models.User
import com.lnct.bhopal.ac.`in`.idealab.R
import com.lnct.bhopal.ac.`in`.idealab.Utils
import com.lnct.bhopal.ac.`in`.idealab.activity.HomeActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {

    private val TAG = "PROFILE"
    val db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView =  inflater.inflate(R.layout.fragment_profile, container, false)

        val user = Utils.getUser(requireContext())

        mView.evRegPhNo.setText(user._id)
        mView.evRegPhNo.isEnabled = false

        mView.evEmail.setText(user.email)
        mView.evEmail.isEnabled = false

        mView.evName.setText(user.name)
        mView.evName.isEnabled = false

        mView.evPhNo.setText(user.whatsapp)

        mView.evEnrollment.setText(user.enrollment)

        mView.evSem.setText(user.sem.toString())

        mView.evAddress.setText(user.address)

        mView.user_name_profile.text = "Hi! "+user.name

        mView.evGitHub.setText(user.github)

        mView.profile_update_button.setOnClickListener {
            val whatsapp = mView.evPhNo.text.toString().trim()
            val enroll = mView.evEnrollment.text.toString().trim()
            val sem = mView.evSem.text.toString().trim()
            val address = mView.evAddress.text.toString().trim()
            val github = mView.evGitHub.text.toString().trim()

            if(whatsapp.length != 0 &&
                    enroll.length != 0 &&
                    sem.length != 0 &&
                    address.length != 0 &&
                    github.length != 0){

                mView.profile_update_button.isEnabled = false
                mView.loading.visibility = View.VISIBLE

                db.collection("users").document(user._id).update(
                    mapOf(
                        "github" to github,
                        "semester" to sem,
                        "address" to address,
                        "enrollment" to enroll,
                        "whatsapp" to whatsapp
                    )
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.github = github
                        user.sem = sem.toInt()
                        user.address = address
                        user.enrollment = enroll
                        user.whatsapp = whatsapp

                        Utils.deleteUser(requireContext())
                        Utils.saveUser(requireContext(),user)

                        Toast.makeText(requireContext(), "Details updated 👍", Toast.LENGTH_SHORT).show()
                        mView.profile_update_button.isEnabled = true
                        mView.loading.visibility = View.GONE
                    }else {
                        Toast.makeText(requireContext(), "Try again later.", Toast.LENGTH_SHORT).show()
                        mView.profile_update_button.isEnabled = true
                        mView.loading.visibility = View.GONE
                    }

                }





            }else Toast.makeText(requireContext(), "Invalid credentials.", Toast.LENGTH_SHORT).show()
        }




        return mView
    }


}