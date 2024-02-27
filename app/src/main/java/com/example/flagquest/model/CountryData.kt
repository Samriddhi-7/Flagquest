package com.example.flagquest.model

import com.google.gson.annotations.SerializedName

data class Country(
	val flags: Flags,
	val name: Name
)

data class Flags(
	val png: String,
	val svg: String,
	val alt: String
)

data class Name(
	val common: String,
	val official: String,
	val nativeName: Map<String, Map<String, String>> // This is a nested map for native names
)

