//
//  AppDelegate.swift

import SwiftUI
import KMMViewModelCore
import KMMViewModelSwiftUI
import nrstations

private var _koin: Koin_coreKoin?
var koin: Koin_coreKoin {
    return _koin!
}

class AppDelegate: NSObject, UIApplicationDelegate {
    static private(set) var instance: AppDelegate! = nil
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        initKoin()
        return true
    }
}

func initKoin() {
    let koinApp = StationsSDKInitializerKt.doInitStationsSDK(
        apiConfig: DefaultAPIConfig.shared.apiConfig,
        enableLogging: true
    )
    NSLog("started SDK")
    _koin = koinApp.koin
}
