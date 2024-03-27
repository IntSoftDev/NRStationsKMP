//
//  AppDelegate.swift

import SwiftUI
import KMMViewModelCore
import KMMViewModelSwiftUI
import nrstations

private var _koin: Koin_coreKoin?
var koin: Koin_coreKoin {
    initKoin()
    return _koin!
}

class AppDelegate: NSObject, UIApplicationDelegate {
    static private(set) var instance: AppDelegate! = nil
    func application(
        _ application: UIApplication,
        continue userActivity: NSUserActivity,
        restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void
    ) -> Bool {
        print("application  restorationHandler")
        return true
    }
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        print("application enter")
        AppDelegate.instance = self
        initKoin()
        return true
    }
}

func initKoin() {
    if (_koin == nil) {
        
        let stationDefaults = UserDefaults(suiteName: "NRSTATIONS_SETTINGS")!
        NSLog("started stations SDK")
        
        let config = APIConfig(serverUrl: "https://onrails-test.azurewebsites.net", licenseKey: "", serverToken: "")
        
        let koinApp = StationsSDKInitializerKt.doInitStationsSDK(
            apiConfig: config,
            userDefaults: stationDefaults,
            enableLogging: true)
        
        NSLog("started SDK")
        
        _koin = koinApp.koin
    }
}
