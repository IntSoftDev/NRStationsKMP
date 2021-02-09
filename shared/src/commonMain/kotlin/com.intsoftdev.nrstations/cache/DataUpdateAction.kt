package com.intsoftdev.nrstations.cache

sealed class DataUpdateAction {
    object REFRESH : DataUpdateAction()
    object LOCAL : DataUpdateAction()
}