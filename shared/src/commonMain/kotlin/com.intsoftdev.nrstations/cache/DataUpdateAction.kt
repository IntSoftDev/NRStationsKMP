package com.intsoftdev.nrstations.cache

internal sealed class DataUpdateAction {
    object REFRESH : DataUpdateAction()
    object LOCAL : DataUpdateAction()
}