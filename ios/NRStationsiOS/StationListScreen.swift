//
//  StationListView.swift
//  Created by Awais Zaka on 01/11/2022.
import Combine
import SwiftUI
import nrstations
import MapKit

class ObservableStationModel: ObservableObject {
    private var viewModel: StationsCallbackViewModel?
    @Published
    var loading = false
    
    @Published
    var stations: [StationLocation]?
    @Published
    var error: String?
    private var cancellables = [AnyCancellable]()
    func activate() {
        NSLog("activate")
        let viewModel = KotlinDependencies.shared.getStationsViewModel()
        doPublish(viewModel.stations) { [weak self] stationsViewState in
            self?.stations = stationsViewState.stations
            self?.error = stationsViewState.error
        }.store(in: &cancellables)
        viewModel.refreshStations()
        self.viewModel = viewModel
    }
    
    func deactivate() {
        NSLog("deactivate")
        cancellables.forEach { $0.cancel() }
        cancellables.removeAll()
    }
    
    func refresh() {
        viewModel?.refreshStations()
    }
    
    deinit {
        viewModel?.clear()
        viewModel = nil
    }
}

struct StationListScreen: View {
    @StateObject
    var observableModel = ObservableStationModel()
    
    var body: some View {
        StationListContent(
            loading: observableModel.loading,
            stations: observableModel.stations,
            error: observableModel.error,
            refresh: { observableModel.refresh() }
        )
        .onAppear(perform: {
            NSLog("onAppear")
            observableModel.activate()
        })
        .onDisappear(perform: {
            NSLog("onDisappear")
            observableModel.deactivate()
        })
    }
}

struct StationListContent: View {
    @StateObject
    var locationManager = LocationManager()
    var loading: Bool
    var stations: [StationLocation]?
    var error: String?
    var refresh: () -> Void
    @State var showNearestView = false
    var body: some View {
        NavigationView {
            ZStack {
                VStack {
                    if let stations = stations {
                        List(stations, id: \.crsCode) { station in
                            NavigationLink {
                                let stationLocation = CLLocationCoordinate2D(latitude: station.latitude, longitude: station.longitude)
                                NearbyStationsScreen(location: stationLocation, stationLocation: station)
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
                        NavigationLink(
                            destination: NearbyStationsScreen(location: locationManager.lastLocation!.coordinate, stationLocation: nil),
                            isActive: $showNearestView
                        ) {
                            
                        }
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
        StationListContent(
            loading: false,
            stations: [
                StationLocation(stationName: "London Waterloo", crsCode: "WAT", latitude: 51.50329, longitude: -0.113108),
                StationLocation(stationName: "Gatwick Airport", crsCode: "GAT", latitude: 51.50329, longitude: -0.113108)
            ],
            error: "Unable to retrieve data",
            refresh: {}
        )
    }
}
