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
    KoinIOSKt.doInitKoinIos(userDefaults: UserDefaults(suiteName: "NRSTATIONS_SETTINGS")!)
}

func startLogger() {
    LoggeriOSKt.debugBuild()
}
