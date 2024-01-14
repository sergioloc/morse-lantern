package com.slc.morse.ui.chat

import android.hardware.camera2.CameraManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slc.morse.data.Dictionary
import com.slc.morse.domain.entities.Message
import com.slc.morse.domain.entities.Symbol
import com.slc.morse.domain.entities.Character
import com.slc.morse.utils.isLetter
import com.slc.morse.utils.isNumber
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val _lanternOn = MutableLiveData<Boolean>()
    val lanternOn: LiveData<Boolean> = _lanternOn

    private val _code = MutableLiveData<String>()
    val code: LiveData<String> = _code

    private val _countdown = MutableLiveData<Float>()
    val countdown: LiveData<Float> = _countdown

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> get() = _messages

    fun start(text: String, cameraManager: CameraManager) {
        val newMessage = Message(text = text, isMine = true)
        val currentMessages = _messages.value ?: emptyList()
        val newMessages = currentMessages + newMessage
        _messages.value = newMessages
        _loading.value = true
        _lanternOn.value = true

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

        viewModelScope.launch {
            val countdown = getMessageTime(characters)
            var timeLeft = countdown
            while (timeLeft > 0) {
                _countdown.value = timeLeft / countdown.toFloat()
                delay(100)
                timeLeft -= 100
            }
        }

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
            _code.value = ""
            _lanternOn.value = false
            _loading.value = false
        }
    }

    private fun getMessageTime(characters: List<Character>): Int {
        var milliseconds = 2000
        for (character in characters) {
            for (symbol in character.symbols) {
                milliseconds += when (symbol) {
                    Symbol.SPACE -> {
                        2000
                    }
                    Symbol.DOT -> {
                        500
                    }
                    Symbol.LINE -> {
                        2000
                    }
                }
            }
        }
        return milliseconds
    }

}