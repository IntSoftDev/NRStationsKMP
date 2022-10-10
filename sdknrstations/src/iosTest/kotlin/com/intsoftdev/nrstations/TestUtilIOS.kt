package com.intsoftdev.nrstations

import co.touchlab.sqliter.DatabaseConfiguration
import com.intsoftdev.nrstations.database.NRStationsDb
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection

internal actual fun testDbConnection(): SqlDriver {
    val schema = NRStationsDb.Schema
    return NativeSqliteDriver(
        DatabaseConfiguration(
            name = "NRStationsDb",
            version = schema.version,
            create = { connection ->
                wrapConnection(connection) { schema.create(it) }
            },
            upgrade = { connection, oldVersion, newVersion ->
                wrapConnection(connection) { schema.migrate(it, oldVersion, newVersion) }
            },
            inMemory = true
        )
    )
}
