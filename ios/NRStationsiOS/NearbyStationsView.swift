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
    @ObservedViewModel var viewModel: NrNearbyViewModel
    
    var location: CLLocationCoordinate2D
    
    init(viewModel: ObservableViewModel<NrNearbyViewModel>.Projection, loc: CLLocationCoordinate2D) {
        self._viewModel = ObservedViewModel(viewModel)
        self.location = loc
    }
    
    var body: some View {
        GeometryReader { proxy in
            VStack {
                MapView(coordinate: location)
                    .frame(height: proxy.size.height/2)
                
                VStack {
                    if let stations = viewModel.uiState.stations?.stationDistances {
                        List(stations, id: \.station.crsCode) { station in
                            NearbyStationsRowView(station: station)
                        }
                    }
                    if let error = viewModel.uiState.error {
                        Text(error)
                            .foregroundColor(.red)
                    }
                }.onAppear {
                    viewModel.getNearbyStations(latitude: location.latitude, longitude: location.longitude)
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
            Text(String(format: "%f miles", station.distanceInMiles))
        }
    }
}
