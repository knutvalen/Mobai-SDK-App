package com.tietoevry.mobaisdkapp

interface LivenessCaptureListener {
    abstract fun onCaptureFinished(captureSessionData: ByteArray)
}