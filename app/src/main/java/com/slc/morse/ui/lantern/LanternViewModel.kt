package com.slc.morse.ui.lantern

import android.hardware.camera2.CameraManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LanternViewModel: ViewModel() {

    private val _lanternOn = MutableLiveData<Boolean>()
    val lanternOn: LiveData<Boolean> = _lanternOn

    fun start(text: String, isPlaying: Boolean, cameraManager: CameraManager) {
        val cameraId = cameraManager.cameraIdList[0]
        cameraManager.setTorchMode(cameraId, !isPlaying)
        _lanternOn.value = !isPlaying
    }

}