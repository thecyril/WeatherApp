package com.example.meteochallenge.core.translator

interface Translator {
	fun getString(id: Int, vararg args: Any): String
}