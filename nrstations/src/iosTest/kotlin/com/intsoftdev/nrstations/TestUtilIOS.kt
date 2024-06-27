package com.intsoftdev.nrstations

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.inMemoryDriver
import com.intsoftdev.nrstations.database.NRStationsDb

internal actual fun testDbConnection(): SqlDriver {
    val schema = NRStationsDb.Schema
    return inMemoryDriver(schema)
}
