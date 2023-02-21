package com.lnct.bhopal.ac.`in`.idealab.frgments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lnct.bhopal.ac.`in`.idealab.R
import com.lnct.bhopal.ac.`in`.idealab.Utils
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_skills.*
import kotlinx.android.synthetic.main.fragment_skills.view.*


class SkillsFragment : BottomSheetDialogFragment(){
    private val TAG = "Skills"
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.fragment_skills, container, false)

        val user = Utils.getUser(requireContext())
        val skills = user.skills

        if (skills.contains("Mobile Development")){
            mView.moblie_development_cb.isChecked = true
        }
        if(skills.contains("Web Development")){
            mView.web_dev_cb.isChecked = true
        }
        if(skills.contains("Graphic Designing")){
            mView.graphic_design_cb.isChecked = true
        }
        if(skills.contains("3D Modeling")){
            mView.threeD_cb.isChecked = true
        }
        if(skills.contains("IOT")){
            mView.iot_cb.isChecked = true
        }
        if(skills.contains("Machine Learning")){
            mView.ml_cb.isChecked = true
        }

        if(skills.contains("UI/UX")){
            mView.uiux_cb.isChecked = true
        }
        if(skills.contains("Blockchain")){
            mView.blockchain_cb.isChecked = true
        }
        if(skills.contains("Electronics")){
            mView.electronics_cb.isChecked = true
        }
        if(skills.contains("Mechanics")){
            mView.mechanics_cb.isChecked = true
        }


        mView.apply_skills.setOnClickListener {
            mView.apply_skills.isEnabled = false
            mView.loading_skills.visibility = View.VISIBLE
            val newList = ArrayList<String>()

            if(moblie_development_cb.isChecked) newList.add("Mobile Development")
            if(web_dev_cb.isChecked) newList.add("Web Development")
            if(graphic_design_cb.isChecked) newList.add("Graphic Designing")
            if(threeD_cb.isChecked) newList.add("3D Modeling")
            if(iot_cb.isChecked) newList.add("IOT")
            if(ml_cb.isChecked) newList.add("Machine Learning")
            if(uiux_cb.isChecked) newList.add("UI/UX")
            if(blockchain_cb.isChecked) newList.add("Blockchain")
            if(electronics_cb.isChecked) newList.add("Electronics")
            if(mechanics_cb.isChecked) newList.add("Mechanics")


            db.collection("users").document(user._id).update(
                mapOf(
                   "skills" to newList
                )
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.skills = newList
                    Utils.deleteUser(requireContext())
                    Utils.saveUser(requireContext(),user)
                    Toast.makeText(requireContext(), "Skills updated 👍", Toast.LENGTH_SHORT).show()
                    mView.apply_skills.isEnabled = true
                    mView.loading_skills.visibility = View.GONE
                }else {
                    Toast.makeText(requireContext(), "Try again later.", Toast.LENGTH_SHORT).show()
                    mView.apply_skills.isEnabled = true
                    mView.loading_skills.visibility = View.GONE
                }

            }


        }


        return mView
    }


}