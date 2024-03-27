//
//  StationListView.swift
//  Created by Awais Zaka on 01/11/2022.
import Combine
import SwiftUI
import nrstations
import MapKit
import KMMViewModelCore
import KMMViewModelSwiftUI

struct StationsListView: View {
    @State var allStations: [StationLocation] = []
    @StateObject var locationManager = LocationManager()
    @State private var searchText = ""
    @State var showNearestView: Bool = false
    var body: some View {
        NavigationStack {
            VStack {
                List(searchResults, id: \.crsCode) { station in
                    NavigationLink {
                        let stationLoc = CLLocationCoordinate2D(latitude: station.latitude, longitude: station.longitude)
                        NearbyStationsContent(location: stationLoc)
                    } label: {
                        StationRowView(station: station)
                    }
                }
            }.navigationTitle("Stations")
                .toolbar {
                    ToolbarItem(placement: .navigationBarTrailing) {
                        if let lastLocation = locationManager.lastLocation {
                            NavigationLink(destination: NearbyStationsContent(location: lastLocation.coordinate)) {
                                Image(systemName: "location")
                            }
                        }
                    }
                }
        }.searchable(text: $searchText)
    }
    var searchResults: [StationLocation] {
        if searchText.isEmpty {
            return allStations
        } else {
            return allStations.filter { $0.stationName.starts(with: searchText) }
        }
    }
}

struct StationsLandingScreen: View {
    @StateViewModel var stationsViewModel = NrStationsViewModel()
    
    @State var allStations: [StationLocation] = []
    var body: some View {
        
        ZStack {
            VStack {
                if let stations = stationsViewModel.uiState.stations {
                    StationsListView(allStations: stations)
                }
                
                if let error = stationsViewModel.uiState.error {
                    Text(error)
                        .foregroundColor(.red)
                }
                Button("Refresh") {
                    stationsViewModel.getAllStations()
                }
            }
            if stationsViewModel.uiState.isLoading { Text("Loading...") }
        }.onAppear(perform: {
            NSLog("onAppear")
            stationsViewModel.getAllStations()
        })
    }
}

struct StationRowView: View {
    var station: StationLocation
    var body: some View {
        HStack {
            Text(station.stationName)
            Spacer()
        }
    }
}



struct StationListScreen_Previews: PreviewProvider {
    static var previews: some View {
        StationsListView(
            allStations: [
                StationLocation(stationName: "London Waterloo", crsCode: "WAT", latitude: 51.50329, longitude: -0.113108),
                StationLocation(stationName: "Gatwick Airport", crsCode: "GAT", latitude: 51.50329, longitude: -0.113108)
            ]
        )
    }
}
