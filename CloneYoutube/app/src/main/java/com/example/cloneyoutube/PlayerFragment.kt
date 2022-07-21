package com.example.cloneyoutube

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.example.cloneyoutube.databinding.FragmentPlayerBinding
import kotlin.math.abs

class PlayerFragment: Fragment(R.layout.fragment_player) {
    private var binding:FragmentPlayerBinding?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentBinding=FragmentPlayerBinding.bind(view)
        binding=fragmentBinding
        fragmentBinding.playerMotionLayout.setTransitionListener(object :MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                    binding?.let {
                        (activity as MainActivity).also {
                            it.findViewById<MotionLayout>(R.id.main_motion_layout).progress=abs(progress)


                        }
                    }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        } )
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}