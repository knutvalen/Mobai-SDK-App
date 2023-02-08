package com.tietoevry.mobaisdkapp

import android.graphics.Bitmap

interface LivenessCaptureListener {
    abstract fun onCaptureFinished(framesCollection: List<Bitmap?>)
}