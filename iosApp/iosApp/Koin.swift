//
//  Koin.swift
//  iosApp
//
//  Created by Awais Zaka on 10/10/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

func startKoin() {
    
    let doOnStartup = {
        NSLog("Calling Swift from Kotlin")
    }
    let userDefaults = UserDefaults(suiteName: "NRSTATIONS_SETTINGS")!
    let koinApplication = KoinIOSKt.doInitKoinIos(userDefaults: userDefaults, doOnStartup: doOnStartup)
    _koin = koinApplication.koin
}

func startLogger() {
    LoggeriOSKt.debugBuild()
}

private var _koin: Koin_coreKoin? = nil
var koin: Koin_coreKoin {
    return _koin!
}
