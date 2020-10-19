package com.intsoftdev.nrstations.shared

expect fun currentTimeMillis(): Long

expect class Platform() {
    val platform: String
}