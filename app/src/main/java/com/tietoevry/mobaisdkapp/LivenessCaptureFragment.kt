package com.tietoevry.mobaisdkapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import bio.mobai.library.biometrics.capturesession.*
import bio.mobai.library.biometrics.enums.MBCaptureSessionStatus
import bio.mobai.library.biometrics.enums.MBFaceStatus
import com.tietoevry.mobaisdkapp.databinding.FragmentLivenessCaptureBinding
import timber.log.Timber

@SuppressLint("UnsafeOptInUsageError")
class LivenessCaptureFragment : Fragment() {
    private var binding: FragmentLivenessCaptureBinding? = null
    private var captureSessionService: MBCaptureSessionService? = null
    private var livenessCaptureListener: LivenessCaptureListener? = null

    private val captureSessionServiceListener = object : MBCaptureSessionServiceListener {
        override fun onCaptureFinished(result: MBCaptureSessionResult?) {
            Timber.i("onCaptureFinished: $result")

            result?.let {
                livenessCaptureListener?.onCaptureFinished(it.captureSessionData)
            }
        }

        override fun onCaptureSessionStatusChanged(captureStatus: MBCaptureSessionStatus) {
            Timber.i("onCaptureSessionStatusChanged: $captureStatus")
        }

        override fun onFailure(errorEnum: MBCaptureSessionError) {
            Timber.i("onFailure: $errorEnum")
        }

        override fun onValidating(faceStatus: MBFaceStatus) {
            Timber.i("onValidating: $faceStatus")
        }

        override fun onCaptureStarted() {
            Timber.i("onCaptureStarted()")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val captureSessionService = MBCaptureSessionService(
            requireContext(),
            this,
            MBCaptureSessionOptions.Builder()
                .automaticCapture(true)
                .cameraSelector(MBCaptureSessionOptions.MBCameraSelector.FRONT_CAMERA)
                .build(),
            captureSessionServiceListener
        )

        this.captureSessionService = captureSessionService
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLivenessCaptureBinding
            .inflate(inflater, container,false)

        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            llCaptureSessionViewContainer.addView(
                captureSessionService?.getCaptureSessionView()
            )
        }
    }

    fun setListener(listener: LivenessCaptureListener) {
        livenessCaptureListener = listener
    }

    fun start() {
        captureSessionService?.startCamera()
        captureSessionService?.startCaptureSession()
    }

}