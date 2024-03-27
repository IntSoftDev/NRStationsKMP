//
//  NearbyStationsView.swift
//  NRStationsiOS
//
//  Created by Awais Zaka on 12/03/2023.
//  Copyright © 2023 IntSoftDev. All rights reserved.
//

import SwiftUI
import nrstations
import KMMViewModelCore
import KMMViewModelSwiftUI
import MapKit

/*
 struct NearbyStationsContent_Previews: PreviewProvider {
 static var previews: some View {
 NearbyStationsContent()
 }
 }*/

struct NearbyStationsContent: View {
    @StateViewModel var nearbyViewModel = NrNearbyViewModel()
    
    var location: CLLocationCoordinate2D
    
    var body: some View {
        GeometryReader { proxy in
            VStack {
                MapView(coordinate: location)
                    .frame(height: proxy.size.height/2)
                
                VStack {
                    if let stations = nearbyViewModel.uiState.stations?.stationDistances {
                        List(stations, id: \.station.crsCode) { station in
                            NearbyStationsRowView(station: station)
                        }
                    }
                    if let error = nearbyViewModel.uiState.error {
                        Text(error)
                            .foregroundColor(.red)
                    }
                }.onAppear {
                    nearbyViewModel.getNearbyStations(latitude: location.latitude, longitude_: location.longitude)
                }.frame(height: proxy.size.height/2)
            }
        }.navigationTitle("Nearby Stations")
            .navigationBarTitleDisplayMode(.inline)
    }
}

struct NearbyStationsRowView: View {
    var station: StationDistance
    var body: some View {
        HStack {
            Text(station.station.stationName)
            Spacer()
            let formattedDistance = String(format: "%.1f miles", station.distanceInMiles)
            Text(formattedDistance)
        }
    }
}
