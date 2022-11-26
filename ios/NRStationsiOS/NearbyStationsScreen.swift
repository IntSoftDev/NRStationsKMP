//
//  NearbyStationsScreen.swift
//  NRStationsiOS
//
//  Created by Awais Zaka on 01/11/2022.
//

import Combine
import SwiftUI
import MapKit
import nrstations

class ObservableNearbyStationsModel: ObservableObject {
    private var viewModel: NearbyCallbackViewModel?
    @Published
    var loading = false
    @Published
    var stations: [StationDistance]?
    @Published
    var error: String?
    private var cancellables = [AnyCancellable]()
    func activate(location: CLLocationCoordinate2D, crsCode: String?) {
        NSLog("activate")
        setupViewModel()
        viewModel?.getNearbyStations(latitude: location.latitude, longitude: location.longitude, crsCode: crsCode)
    }
    func deactivate() {
        NSLog("deactivate")
        cancellables.forEach { $0.cancel() }
        cancellables.removeAll()
        // don't destroy the viewModel here
    }
    func setupViewModel() {
        let viewModel = KotlinDependencies.shared.getNearbyViewModel()
        doPublish(viewModel.stations) { [weak self] stationsViewState in
            self?.stations = stationsViewState.stations?.stationDistances
            self?.error = stationsViewState.error
        }.store(in: &cancellables)
        self.viewModel = viewModel
    }
    deinit {
        NSLog("ObservableNearbyStationsModel deinit")
        // The viewModel is recreated when the screen is re-entered
        viewModel?.clear()
        viewModel = nil
    }
}

struct NearbyStationsScreen: View {
    @StateObject
    var observableModel = ObservableNearbyStationsModel()
    var location: CLLocationCoordinate2D
    var stationLocation: StationLocation?
    var body: some View {
        GeometryReader { proxy in
            VStack {
                MapView(coordinate: location)
                    .frame(height: proxy.size.height/2)
                NearbyStationsContent(
                    loading: observableModel.loading,
                    stations: observableModel.stations,
                    error: observableModel.error,
                    refresh: {  }
                )
                .onAppear(perform: {
                    NSLog("onAppear")
                    observableModel.activate(location: location, crsCode: stationLocation?.crsCode ?? nil)
                })
                .onDisappear(perform: {
                    NSLog("onDisappear")
                    observableModel.deactivate()
                }).frame(height: proxy.size.height/2)
            }
        }.navigationTitle((stationLocation == nil ? "Nearby Stations" : stationLocation?.stationName)!)
            .navigationBarTitleDisplayMode(.inline)
    }
}

struct NearbyStationsContent: View {
    var loading: Bool
    var stations: [StationDistance]?
    var error: String?
    var refresh: () -> Void
    var body: some View {
        VStack {
            if let stations = stations {
                List(stations, id: \.station.crsCode) { station in
                    NearbyStationsRowView(station: station)
                }
            }
            if let error = error {
                Text(error)
                    .foregroundColor(.red)
            }
        }
        if loading { Text("Loading...") }
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

/*struct StationDetail_Previews: PreviewProvider {
    static var previews: some View {
        NearbyStationsScreen(station: StationLocation(stationName: "London Waterloo", crsCode: "WAT", latitude: 51.50329, longitude: -0.113108))
    }
}*/
