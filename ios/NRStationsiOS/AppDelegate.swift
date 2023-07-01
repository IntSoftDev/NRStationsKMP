//
//  AppDelegate.swift

import SwiftUI
import KMMViewModelCore
import KMMViewModelSwiftUI
import nrstations

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    @StateViewModel var stationsViewModel = NrStationsViewModel()
    @StateViewModel var nearbyViewModel = NrNearbyViewModel()
    
    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions
        launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

        let defaults = UserDefaults(suiteName: "NRSTATIONS_SETTINGS")!
        
        let config = APIConfig(serverUrl: "https://onrails-test.azurewebsites.net", licenseKey: "")
        
        StationsSDKInitializerKt.doInitStationsSDK(
            apiConfig: config,
            userDefaults: defaults,
            enableLogging: true)
        
        NSLog("started SDK")

        let viewController = UIHostingController(rootView: StationListScreen(stationsViewModel: $stationsViewModel, nearbyViewModel: $nearbyViewModel))

        self.window = UIWindow(frame: UIScreen.main.bounds)
        self.window?.rootViewController = viewController
        self.window?.makeKeyAndVisible()

        return true
    }
}
