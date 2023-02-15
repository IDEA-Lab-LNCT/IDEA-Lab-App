package com.lnct.bhopal.ac.`in`.idealab.frgments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.jesusd0897.gallerydroid.model.GalleryDroid
import com.jesusd0897.gallerydroid.model.Picture
import com.jesusd0897.gallerydroid.view.fragment.GalleryFragment
import com.lnct.bhopal.ac.`in`.idealab.R
import kotlinx.android.synthetic.main.fragment_highlights.*

class HighlightsFragment : Fragment() {

    private val TAG = "HIGH"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView =  inflater.inflate(R.layout.fragment_highlights, container, false)
        launchHighlights()
        return mView
    }



    fun launchHighlights(){
        Log.d(TAG,"launching . . ..")
        requireActivity().supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) //Optional
            .replace(R.id.navHostFragment, GalleryFragment.newInstance())
            .commit()

    }



}