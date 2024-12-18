//
//  NrStationsApp.swift
//  NRStationsiOS
//
//  Created by Awais Zaka on 19/03/2024.
//

import SwiftUI

@main
struct NrStationsApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    var body: some Scene {
        WindowGroup {
            StationsLandingScreen()
        }
    }
}
