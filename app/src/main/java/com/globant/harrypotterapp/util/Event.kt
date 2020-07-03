package com.globant.harrypotterapp.util

open class Event<out T>(private val content: T) {
    fun peekContent(): T = content
}
