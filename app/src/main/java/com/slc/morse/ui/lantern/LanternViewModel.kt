package com.slc.morse.ui.lantern

import android.hardware.camera2.CameraManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slc.morse.data.Dictionary
import com.slc.morse.domain.entities.Character
import com.slc.morse.domain.entities.Symbol
import com.slc.morse.utils.isLetter
import com.slc.morse.utils.isNumber
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LanternViewModel: ViewModel() {

    private val _lanternOn = MutableLiveData<Boolean>()
    val lanternOn: LiveData<Boolean> = _lanternOn

    private val _code = MutableLiveData<String>()
    val code: LiveData<String> = _code

    fun start(text: String, cameraManager: CameraManager) {

        val cameraId = cameraManager.cameraIdList[0]
        val characters = ArrayList<Character>()

        for (c: Char in text) {
            if (c == ' ') {
                characters.add(Character(" ", listOf(Symbol.SPACE)))
            } else if (c.toString().isLetter()) {
                Dictionary.letters[c.toString()]?.let { characters.add(it) }
            } else if (c.toString().isNumber()) {
                Dictionary.numbers[c.toString()]?.let { characters.add(it) }
            }
        }

        _lanternOn.value = true

        viewModelScope.launch {
            for (character: Character in characters) {
                _code.value = character.code
                for (symbol: Symbol in character.symbols) {
                    when (symbol) {
                        Symbol.SPACE -> {
                            delay(2000)
                        }
                        Symbol.DOT -> {
                            //cameraManager.setTorchMode(cameraId, true)
                            delay(500)
                            //cameraManager.setTorchMode(cameraId, false)
                        }
                        Symbol.LINE -> {
                            //cameraManager.setTorchMode(cameraId, true)
                            delay(2000)
                            //cameraManager.setTorchMode(cameraId, false)
                        }
                    }
                    delay(1000)
                }
            }
            //cameraManager.setTorchMode(cameraId, false)
            _code.value = "";
            _lanternOn.value = false
        }
    }

}