package com.slc.morse.domain.entities

data class Word(val letter: String, var signals: List<Signal>)
