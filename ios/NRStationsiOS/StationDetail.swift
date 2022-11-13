//
//  StationDetail.swift
//  NRStationsiOS
//
//  Created by Awais Zaka on 01/11/2022.
//

import Combine
import SwiftUI
import MapKit
import nrstations

class ObservableNearbyModel: ObservableObject {
    private var viewModel: NearbyCallbackViewModel?
    
    @Published
    var loading = false
    
    @Published
    var stations: [StationsDistance]?
    
    @Published
    var error: String?
    
    private var cancellables = [AnyCancellable]()
    
    func activate(crsCode: String) {
        let viewModel = KotlinDependencies.shared.getNearbyViewModel()
        
        doPublish(viewModel.stations) { [weak self] stationsViewState in
            self?.stations = stationsViewState.stations?.values.first
            self?.error = stationsViewState.error
        }.store(in: &cancellables)
        
        viewModel.getNearbyStations(crsCode: crsCode)
       
        self.viewModel = viewModel
    }
    
    func deactivate() {
        cancellables.forEach { $0.cancel() }
        cancellables.removeAll()
        
        viewModel?.clear()
        viewModel = nil
    }
}

struct StationDetail: View {
    
    @StateObject
    var observableModel = ObservableNearbyModel()
   
    var station: StationLocation
    
    var body: some View {
        VStack {
            MapView(coordinate: CLLocationCoordinate2DMake(station.latitude, station.longitude))
                .ignoresSafeArea(edges: .top)
                .frame(height: 300)
            
            VStack(alignment: .leading) {
                Text(station.stationName)
                    .font(.title)
                Divider()
            }
            .padding()
            Spacer()
            StationDetailsContent(
                loading: observableModel.loading,
                stations: observableModel.stations,
                error: observableModel.error,
                refresh: {  }
            ).frame(height: 300)
            .onAppear(perform: {
                observableModel.activate(crsCode: station.crsCode)
            })
            .onDisappear(perform: {
                observableModel.deactivate()
            })
        }
        .navigationTitle(station.stationName)
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct StationDetailsContent: View {
    var loading: Bool
    var stations: [StationsDistance]?
    var error: String?
    var refresh: () -> Void
    
    var body: some View {
        NavigationView {
            ZStack {
                VStack {
                    if let stations = stations {
                        
                        List(stations, id: \.station.crsCode) { station in
                            DetailRowView(station: station)
                            
                        }
                        .navigationTitle("Stations")
                    }
                    
                    if let error = error {
                        Text(error)
                            .foregroundColor(.red)
                    }
                    Button("Refresh") {
                        refresh()
                    }
                }
                if loading { Text("Loading...") }
            }
        }
    }
}

struct DetailRowView: View {
    var station: StationsDistance
    
    var body: some View {
        HStack {
            Text(station.station.stationName)
            Spacer()
            Text(String(format: "%f miles", station.distanceFromRefInMiles))
        }
    }
}

struct StationDetail_Previews: PreviewProvider {
    static var previews: some View {
        StationDetail(station: StationLocation(stationName: "London Waterloo", crsCode: "WAT", latitude: 51.50329, longitude: -0.113108))
    }
}
