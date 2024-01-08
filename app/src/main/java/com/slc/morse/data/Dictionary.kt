package com.slc.morse.data

import com.slc.morse.domain.entities.Signal

class Dictionary {

    companion object {
        val letters = mapOf(
            "a" to listOf(Signal.DOT, Signal.LINE),
            "b" to listOf(Signal.LINE, Signal.DOT, Signal.DOT, Signal.DOT),
            "c" to listOf(Signal.LINE, Signal.DOT, Signal.LINE, Signal.DOT),
            "d" to listOf(Signal.LINE, Signal.DOT, Signal.DOT),
            "e" to listOf(Signal.DOT),
            "f" to listOf(Signal.DOT, Signal.DOT, Signal.LINE, Signal.DOT),
            "g" to listOf(Signal.LINE, Signal.LINE, Signal.DOT),
            "h" to listOf(Signal.DOT, Signal.DOT, Signal.DOT, Signal.DOT),
            "i" to listOf(Signal.DOT, Signal.DOT),
            "j" to listOf(Signal.DOT, Signal.LINE, Signal.LINE, Signal.LINE),
            "k" to listOf(Signal.LINE, Signal.DOT, Signal.LINE),
            "l" to listOf(Signal.DOT, Signal.LINE, Signal.DOT, Signal.DOT),
            "m" to listOf(Signal.LINE, Signal.LINE),
            "n" to listOf(Signal.LINE, Signal.DOT),
            "o" to listOf(Signal.LINE, Signal.LINE, Signal.LINE),
            "p" to listOf(Signal.DOT, Signal.LINE, Signal.LINE, Signal.DOT),
            "q" to listOf(Signal.LINE, Signal.LINE, Signal.DOT, Signal.LINE),
            "r" to listOf(Signal.DOT, Signal.LINE, Signal.DOT),
            "s" to listOf(Signal.DOT, Signal.DOT, Signal.DOT),
            "t" to listOf(Signal.LINE),
            "u" to listOf(Signal.DOT, Signal.DOT, Signal.LINE),
            "v" to listOf(Signal.DOT, Signal.DOT, Signal.DOT, Signal.LINE),
            "w" to listOf(Signal.DOT, Signal.LINE, Signal.LINE),
            "x" to listOf(Signal.LINE, Signal.DOT, Signal.DOT, Signal.LINE),
            "y" to listOf(Signal.LINE, Signal.DOT, Signal.LINE, Signal.LINE),
            "z" to listOf(Signal.LINE, Signal.LINE, Signal.DOT, Signal.DOT),
        )

        val numbers = mapOf(
            "1" to listOf(Signal.DOT, Signal.LINE, Signal.LINE, Signal.LINE, Signal.LINE),
            "2" to listOf(Signal.DOT, Signal.DOT, Signal.LINE, Signal.LINE, Signal.LINE),
            "3" to listOf(Signal.DOT, Signal.DOT, Signal.DOT, Signal.LINE, Signal.LINE),
            "4" to listOf(Signal.DOT, Signal.DOT, Signal.DOT, Signal.DOT, Signal.LINE),
            "5" to listOf(Signal.DOT, Signal.DOT, Signal.DOT, Signal.DOT, Signal.DOT),
            "6" to listOf(Signal.LINE, Signal.DOT, Signal.DOT, Signal.DOT, Signal.DOT),
            "7" to listOf(Signal.LINE, Signal.LINE, Signal.DOT, Signal.DOT, Signal.DOT),
            "8" to listOf(Signal.LINE, Signal.LINE, Signal.LINE, Signal.DOT, Signal.DOT),
            "9" to listOf(Signal.LINE, Signal.LINE, Signal.LINE, Signal.LINE, Signal.DOT),
            "0" to listOf(Signal.LINE, Signal.LINE, Signal.LINE, Signal.LINE, Signal.LINE),
        )
    }

}