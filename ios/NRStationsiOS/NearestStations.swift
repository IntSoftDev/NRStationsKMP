//
//  NearestStations.swift
//  NRStationsiOS
//
//  Created by Awais Zaka on 04/11/2022.
//

import SwiftUI
import nrstations


struct NearestStations: View {
    @StateObject var locationManager = LocationManager()
    
    var userLatitude: String {
        return "\(locationManager.lastLocation?.coordinate.latitude ?? 0)"
    }
    
    var userLongitude: String {
        return "\(locationManager.lastLocation?.coordinate.longitude ?? 0)"
    }
    
    var body: some View {
        VStack {
            Text("location status: \(locationManager.statusString)")
            HStack {
                Text("latitude: \(userLatitude)")
                Text("longitude: \(userLongitude)")
            }
        }
    }
}

struct NearestStations_Previews: PreviewProvider {
    static var previews: some View {
        NearestStations()
    }
}
