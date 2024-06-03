//
//  NearbyStationsView.swift
//  NRStationsiOS
//
//  Created by Awais Zaka on 12/03/2023.
//  Copyright © 2023 IntSoftDev. All rights reserved.
//

import SwiftUI
import nrstations
import KMPObservableViewModelCore
import KMPObservableViewModelSwiftUI
import MapKit

struct NearbyStationsContent: View {
    @StateViewModel var nearbyViewModel = NrNearbyViewModel()
    let station: StationLocation
    var body: some View {
        let stationLoc = CLLocationCoordinate2D(latitude: station.latitude, longitude: station.longitude)
        GeometryReader { proxy in
            VStack {
                MapView(coordinate: stationLoc)
                    .frame(height: proxy.size.height/2)
                VStack {
                    switch nearbyViewModel.uiState {
                    case _ as NearbyUiStateLoading:
                        Text("Loading...")
                    case let uiState as NearbyUiStateLoaded:
                        let stations = uiState.stations.stationDistances
                        let filteredStations = stations.filter { station in
                            return station.station.crsCode != self.station.crsCode
                        }
                        List(filteredStations, id: \.station.crsCode) { station in
                            NearbyStationsRowView(station: station)
                        } .padding(EdgeInsets(top: 6, leading: 0, bottom: 6, trailing: 0))
                            .listStyle(PlainListStyle())
                    case let uiState as NearbyUiStateError:
                        if let error = uiState.error {
                            Text(error)
                                .foregroundColor(.red)
                        }
                    default:
                        Text("Loading...")
                    }
                }.onAppear {
                    nearbyViewModel.getNearbyStations(crsCode: station.crsCode)
                }.frame(height: proxy.size.height/2)
            }
        }.navigationTitle("Stations near \(station.stationName)")
            .navigationBarTitleDisplayMode(.inline)
    }
}

struct NearbyStationsRowView: View {
    let station: StationDistance
    var body: some View {
        HStack {
            Text(station.station.stationName)
            Spacer()
            let formattedDistance = String(format: "%.1f miles", station.distanceInMiles)
            Text(formattedDistance)
        }
    }
}

struct NearbyStationsContent_Previews: PreviewProvider {
    static var station = StationLocation(stationName: "London Waterloo", crsCode: "WAT", latitude: 51.50329, longitude: -0.113108)
    static var previews: some View {
        NearbyStationsContent(nearbyViewModel: NrNearbyViewModel(), station: station)
    }
}
