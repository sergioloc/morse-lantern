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

        _messages.value = addMessage(text, isMine = true)
        _loading.value = true
        _lanternOn.value = true

        val cameraId = cameraManager.cameraIdList[0]
        val characters = ArrayList<Character>()

        for (c: Char in text) {
            var isSpace = false
            if (c == ' ') {
                characters.add(Character(" ", listOf(Symbol.SPACE)))
                isSpace = true
            } else if (c.toString().isLetter()) {
                Dictionary.letters[c.toString()]?.let { characters.add(it) }
            } else if (c.toString().isNumber()) {
                Dictionary.numbers[c.toString()]?.let { characters.add(it) }
            }
            if (text.last() != c && !isSpace) {
                characters.add(Character("/", listOf(Symbol.SLASH)))
            }
        }

        viewModelScope.launch {
            val symbols = getAllSymbols(characters)
            _messages.value = addMessage(symbolsToText(symbols), isMine = false)
            val countdown = getMessageTime(symbols)
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
                        Symbol.SPACE -> {
                            delay(2000)
                        }
                        Symbol.SLASH -> {
                            delay(0)
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