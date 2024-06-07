package com.slc.morse.data

import com.slc.morse.domain.entities.Symbol
import com.slc.morse.domain.entities.Character

class Dictionary {

    companion object {
        val letters = mapOf(
            "a" to Character("• ─", listOf(Symbol.DOT, Symbol.LINE)),
            "b" to Character("─ • • •", listOf(Symbol.LINE, Symbol.DOT, Symbol.DOT, Symbol.DOT)),
            "c" to Character("─ • ─ •", listOf(Symbol.LINE, Symbol.DOT, Symbol.LINE, Symbol.DOT)),
            "d" to Character("─ • •", listOf(Symbol.LINE, Symbol.DOT, Symbol.DOT)),
            "e" to Character("•", listOf(Symbol.DOT)),
            "f" to Character("• • ─ •", listOf(Symbol.DOT, Symbol.DOT, Symbol.LINE, Symbol.DOT)),
            "g" to Character("─ ─ •", listOf(Symbol.LINE, Symbol.LINE, Symbol.DOT)),
            "h" to Character("• • • •", listOf(Symbol.DOT, Symbol.DOT, Symbol.DOT, Symbol.DOT)),
            "i" to Character("• •", listOf(Symbol.DOT, Symbol.DOT)),
            "j" to Character("• ─ ─ ─", listOf(Symbol.DOT, Symbol.LINE, Symbol.LINE, Symbol.LINE)),
            "k" to Character("─ • ─", listOf(Symbol.LINE, Symbol.DOT, Symbol.LINE)),
            "l" to Character("• ─ • •", listOf(Symbol.DOT, Symbol.LINE, Symbol.DOT, Symbol.DOT)),
            "m" to Character("─ ─", listOf(Symbol.LINE, Symbol.LINE)),
            "n" to Character("─ •", listOf(Symbol.LINE, Symbol.DOT)),
            "o" to Character("─ ─ ─", listOf(Symbol.LINE, Symbol.LINE, Symbol.LINE)),
            "p" to Character("• ─ ─ •", listOf(Symbol.DOT, Symbol.LINE, Symbol.LINE, Symbol.DOT)),
            "q" to Character("─ ─ • ─", listOf(Symbol.LINE, Symbol.LINE, Symbol.DOT, Symbol.LINE)),
            "r" to Character("• ─ •", listOf(Symbol.DOT, Symbol.LINE, Symbol.DOT)),
            "s" to Character("• • •", listOf(Symbol.DOT, Symbol.DOT, Symbol.DOT)),
            "t" to Character("─", listOf(Symbol.LINE)),
            "u" to Character("• • ─", listOf(Symbol.DOT, Symbol.DOT, Symbol.LINE)),
            "v" to Character("• • • ─", listOf(Symbol.DOT, Symbol.DOT, Symbol.DOT, Symbol.LINE)),
            "w" to Character("• ─ ─", listOf(Symbol.DOT, Symbol.LINE, Symbol.LINE)),
            "x" to Character("─ • • ─", listOf(Symbol.LINE, Symbol.DOT, Symbol.DOT, Symbol.LINE)),
            "y" to Character("─ • ─ ─", listOf(Symbol.LINE, Symbol.DOT, Symbol.LINE, Symbol.LINE)),
            "z" to Character("─ ─ • •", listOf(Symbol.LINE, Symbol.LINE, Symbol.DOT, Symbol.DOT)),
        )

        val numbers = mapOf(
            "0" to Character("─ ─ ─ ─ ─", listOf(Symbol.LINE, Symbol.LINE, Symbol.LINE, Symbol.LINE, Symbol.LINE)),
            "1" to Character("• ─ ─ ─ ─", listOf(Symbol.DOT, Symbol.LINE, Symbol.LINE, Symbol.LINE, Symbol.LINE)),
            "2" to Character("• • ─ ─ ─", listOf(Symbol.DOT, Symbol.DOT, Symbol.LINE, Symbol.LINE, Symbol.LINE)),
            "3" to Character("• • • ─ ─", listOf(Symbol.DOT, Symbol.DOT, Symbol.DOT, Symbol.LINE, Symbol.LINE)),
            "4" to Character("• • • • ─", listOf(Symbol.DOT, Symbol.DOT, Symbol.DOT, Symbol.DOT, Symbol.LINE)),
            "5" to Character("• • • • •", listOf(Symbol.DOT, Symbol.DOT, Symbol.DOT, Symbol.DOT, Symbol.DOT)),
            "6" to Character("─ • • • •", listOf(Symbol.LINE, Symbol.DOT, Symbol.DOT, Symbol.DOT, Symbol.DOT)),
            "7" to Character("─ ─ • • •", listOf(Symbol.LINE, Symbol.LINE, Symbol.DOT, Symbol.DOT, Symbol.DOT)),
            "8" to Character("─ ─ ─ • •", listOf(Symbol.LINE, Symbol.LINE, Symbol.LINE, Symbol.DOT, Symbol.DOT)),
            "9" to Character("─ ─ ─ ─ •", listOf(Symbol.LINE, Symbol.LINE, Symbol.LINE, Symbol.LINE, Symbol.DOT)),
        )
    }

}