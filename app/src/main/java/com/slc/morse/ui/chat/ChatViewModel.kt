package com.slc.morse.ui.chat

import android.hardware.camera2.CameraManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slc.morse.data.Dictionary
import com.slc.morse.data.Speed
import com.slc.morse.domain.entities.Message
import com.slc.morse.domain.entities.Symbol
import com.slc.morse.domain.entities.Character
import com.slc.morse.utils.isLetter
import com.slc.morse.utils.isNumber
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val _lightOn = MutableLiveData<Boolean>()
    val lightOn: LiveData<Boolean> = _lightOn

    private val _code = MutableLiveData<String>()
    val code: LiveData<String> = _code

    private val _countdown = MutableLiveData<Float>()
    val countdown: LiveData<Float> = _countdown

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> get() = _messages

    private var _run = false

    fun start(text: String, cameraManager: CameraManager) {
        _run = true
        _messages.value = addMessage(text, isMine = true)
        //_loading.value = true
        _lightOn.value = true

        val characters = ArrayList<Character>()

        for (i in text.indices) {
            val c = text[i]
            var isSpace = false

            if (c == ' ') {
                characters.add(Character(" ", listOf(Symbol.SPACE)))
                isSpace = true
            } else if (c.toString().isLetter()) {
                Dictionary.letters[c.toString()]?.let { characters.add(it) }
            } else if (c.toString().isNumber()) {
                Dictionary.numbers[c.toString()]?.let { characters.add(it) }
            }
            if (i < text.length-1 && !isSpace && (i+1 < text.length && text[i+1] != ' ')) {
                characters.add(Character("/", listOf(Symbol.SLASH)))
            }
        }

        viewModelScope.launch {
            val symbols = getAllSymbols(characters)
            val message = symbolsToText(symbols)
            if (message.isNotEmpty()) {
                _messages.value = addMessage(message, isMine = false)
                /*val countdown = getMessageTime(symbols)
                var timeLeft = countdown
                while (timeLeft > 0) {
                    _countdown.value = timeLeft / countdown.toFloat()
                    delay(100)
                    timeLeft -= 100
                }*/
            }
        }

        if (cameraManager.cameraIdList.isEmpty()) {
            return
        }
        else {
            val cameraId = cameraManager.cameraIdList[0]

            viewModelScope.launch {
                try {
                    for (character: Character in characters) {
                        if (!_run) {
                            _loading.value = false
                            _lightOn.value = false
                            cameraManager.setTorchMode(cameraId, false)
                            break
                        }
                        _code.value = character.code
                        for (symbol: Symbol in character.symbols) {
                            if (!_run) {
                                _loading.value = false
                                _lightOn.value = false
                                cameraManager.setTorchMode(cameraId, false)
                                break
                            }
                            when (symbol) {
                                Symbol.DOT -> {
                                    cameraManager.setTorchMode(cameraId, getBoolean(_run, true))
                                    delay(Speed.DOT)
                                    cameraManager.setTorchMode(cameraId, getBoolean(_run, false))
                                }
                                Symbol.LINE -> {
                                    cameraManager.setTorchMode(cameraId, getBoolean(_run, true))
                                    delay(Speed.LINE)
                                    cameraManager.setTorchMode(cameraId, getBoolean(_run, false))
                                }
                                Symbol.SPACE -> {
                                    delay(Speed.SPACE)
                                }
                                Symbol.SLASH -> {
                                    delay(Speed.SLASH)
                                }
                            }
                            delay(Speed.WAIT)
                        }
                    }
                    cameraManager.setTorchMode(cameraId, false)
                } catch (e: Exception) {
                    Log.e("ChatViewModel", "Unable to use camera torch")
                }
                _code.value = ""
                _lightOn.value = false
                _loading.value = false
            }
        }
    }

    fun stop() {
        _run = false
    }

    private fun getBoolean(run: Boolean, value: Boolean): Boolean {
        return run && value
    }

    private fun addMessage(text: String, isMine: Boolean): List<Message> {
        val newMessage = Message(text = text, isMine = isMine)
        val currentMessages = _messages.value ?: emptyList()
        return currentMessages + newMessage
    }

    private fun getAllSymbols(characters: List<Character>): List<Symbol> {
        val list = arrayListOf<Symbol>()
        for (character in characters) {
            list.addAll(character.symbols)
        }
        return list
    }

    private fun symbolsToText(symbols: List<Symbol>): String {
        var text = ""
        for (symbol in symbols) {
            text += when (symbol) {
                Symbol.DOT -> {
                    "•"
                }
                Symbol.LINE -> {
                    "–"
                }
                Symbol.SPACE -> {
                    "   "
                }
                Symbol.SLASH -> {
                    "/"
                }
            }
        }
        return text
    }

    private fun getMessageTime(symbols: List<Symbol>): Int {
        var milliseconds = 2000
        for (symbol in symbols) {
            milliseconds += when (symbol) {
                Symbol.DOT -> {
                    500
                }
                Symbol.LINE -> {
                    2000
                }
                Symbol.SPACE -> {
                    2000
                }
                Symbol.SLASH -> {
                    0
                }
            }
        }
        return milliseconds
    }

}