//
//  StationListView.swift

import Combine
import SwiftUI
import nrstations

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
        let viewModel = KotlinDependencies.shared.getStationsViewModel()
        viewModel.refreshStations()
        
        doPublish(viewModel.stations) { [weak self] stationsViewState in
            self?.stations = stationsViewState.stations
            self?.error = stationsViewState.error
        }.store(in: &cancellables)
        
        self.viewModel = viewModel
    }
    
    func deactivate() {
        cancellables.forEach { $0.cancel() }
        cancellables.removeAll()
        
        viewModel?.clear()
        viewModel = nil
    }
    
    func refresh() {
        viewModel?.refreshStations()
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
            observableModel.activate()
        })
        .onDisappear(perform: {
            observableModel.deactivate()
        })
    }
}

struct StationListContent: View {
    var loading: Bool
    var stations: [StationLocation]?
    var error: String?
    var refresh: () -> Void
    
    var body: some View {
        ZStack {
            VStack {
                if let stations = stations {
                    NavigationView {
                        List(stations, id: \.crsCode) { station in
                            NavigationLink {
                                StationDetail(station: station)
                            } label: {
                                StationRowView(station: station)
                            }
                            
                        }
                        .navigationTitle("Stations")
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
