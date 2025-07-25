package com.intsoftdev.nrstations.mock

import kotlin.time.Clock
import kotlin.time.Instant

class ClockMock(var currentInstant: Instant) : Clock {
    override fun now(): Instant = currentInstant
}
