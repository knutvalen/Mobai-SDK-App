package com.tietoevry.mobaisdkapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.tietoevry.mobaisdkapp.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : Fragment(), LivenessCaptureListener {
    private var binding: FragmentMainBinding? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        binding?.let {
            if (isGranted) {
                startLivenessCapture()
            } else {
                Snackbar.make(
                    it.constraintLayout,
                    R.string.Camera_permission_request_was_denied_,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            val snackBar = binding?.root?.let {
                Snackbar.make(
                    it, R.string.Camera_access_is_required_,
                    Snackbar.LENGTH_INDEFINITE)
            }

            snackBar?.setAction(R.string.Allow) {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            snackBar?.show()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startLivenessCapture() {
        val livenessCaptureFragment: LivenessCaptureFragment = childFragmentManager
            .findFragmentById(R.id.captureSessionContainer) as LivenessCaptureFragment

        livenessCaptureFragment.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding
            .inflate(inflater, container, false)

        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val livenessCaptureFragment: LivenessCaptureFragment = childFragmentManager
            .findFragmentById(R.id.captureSessionContainer) as LivenessCaptureFragment

        livenessCaptureFragment.setListener(this@MainFragment)
    }

    override fun onStart() {
        super.onStart()

        val cameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )

        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            startLivenessCapture()
        } else {
            requestCameraPermission()
        }
    }

    override fun onCaptureFinished(captureSessionData: ByteArray) {
        val priority = if (captureSessionData.isEmpty()) { Log.ERROR } else { Log.INFO }
        Timber.log(priority, "captureSessionData Size: ${captureSessionData.size}")
        Timber.log(priority, "captureSessionData as String: ${captureSessionData.toString(Charsets.UTF_8)}")
    }

}