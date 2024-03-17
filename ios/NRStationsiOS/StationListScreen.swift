//
//  StationListView.swift
//  Created by Awais Zaka on 01/11/2022.
import Combine
import SwiftUI
import nrstations
import MapKit
import KMMViewModelCore
import KMMViewModelSwiftUI

struct StationListScreen: View {
    @ObservedViewModel var stationsViewModel: NrStationsViewModel
    @ObservedViewModel var nearbyViewModel: NrNearbyViewModel
    
    @StateObject
    var locationManager = LocationManager()
    
    @State var showNearestView = false
    
    init(stationsViewModel: ObservableViewModel<NrStationsViewModel>.Projection,
         nearbyViewModel: ObservableViewModel<NrNearbyViewModel>.Projection) {
        self._stationsViewModel = ObservedViewModel(stationsViewModel)
        self._nearbyViewModel = ObservedViewModel(nearbyViewModel)
    }
    
    var body: some View {
        NavigationView {
            ZStack {
                VStack {
                    if let stations = stationsViewModel.uiState.stations {
                        List(stations, id: \.crsCode) { station in
                            NavigationLink {
                                let stationLocation = CLLocationCoordinate2D(latitude: station.latitude, longitude: station.longitude)
                                NearbyStationsContent(viewModel: $nearbyViewModel, loc: stationLocation)
                            } label: {
                                StationRowView(station: station)
                            }
                        }
                        .navigationTitle("Stations")
                        .toolbar {
                            ToolbarItem(placement: .navigationBarTrailing) {
                                Button(action: {
                                    print("button touched")
                                    self.showNearestView = true
                                }, label: {
                                    Image(systemName: "location")
                                })
                            }
                        }
                        if let lastLocation = locationManager.lastLocation {
                            NavigationLink(
                                destination: NearbyStationsContent(viewModel: $nearbyViewModel, loc: lastLocation.coordinate),
                                isActive: $showNearestView
                            ) {
                                
                            }
                        }
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
            }
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
/*
 struct StationListScreen_Previews: PreviewProvider {
 static var previews: some View {
 StationListContent(
 viewModel: NrNearbyViewModel(),
 loading: false,
 stations: [
 StationLocation(stationName: "London Waterloo", crsCode: "WAT", latitude: 51.50329, longitude: -0.113108),
 StationLocation(stationName: "Gatwick Airport", crsCode: "GAT", latitude: 51.50329, longitude: -0.113108)
 ],
 error: "Unable to retrieve data",
 refresh: {}
 )
 }
 }*/
