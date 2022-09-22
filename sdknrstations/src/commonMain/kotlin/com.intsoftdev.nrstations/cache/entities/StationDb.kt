package com.intsoftdev.nrstations.cache.entities

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class StationDb: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var stationName: String = ""
    var crsCode: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
}