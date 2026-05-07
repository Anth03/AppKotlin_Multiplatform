package com.example.applicationcompose

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform