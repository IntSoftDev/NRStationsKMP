//
//  AppDelegate.swift

import SwiftUI
import sdknrstations

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions
        launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

        let defaults = UserDefaults(suiteName: "NRSTATIONS_SETTINGS")!
        
        SDKInitializerKt.doInitStationsSDK(
            userDefaults: defaults,
            koinApp: nil,
            enableLogging: true)
        
        NSLog("started SDK")

        let viewController = UIHostingController(rootView: StationListScreen())

        self.window = UIWindow(frame: UIScreen.main.bounds)
        self.window?.rootViewController = viewController
        self.window?.makeKeyAndVisible()

        return true
    }
}
