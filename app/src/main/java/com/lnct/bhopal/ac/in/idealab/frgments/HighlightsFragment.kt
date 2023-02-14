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

    override fun onAttachFragment(fragment: Fragment) {
        Log.d(TAG,"ON ATTACH CALLED")
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
                        ), Picture(
                            fileURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h3",
                            fileThumbURL = "android.resource://com.lnct.bhopal.ac.in.idealab/drawable/h3",
                        ), Picture(
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