package com.example.meteochallenge.core.translator

import android.content.Context

class ContextTranslator(private val context: Context) : Translator {

	override fun getString(id: Int, vararg args: Any): String =
			if (args.isEmpty()) {
				context.getString(id)
			} else {
				String.format(context.getString(id), *args)
			}
}