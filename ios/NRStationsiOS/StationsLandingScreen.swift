//
//  StationListView.swift
//  Created by Awais Zaka on 01/11/2022.
import Combine
import SwiftUI
import nrstations
import MapKit
import KMPObservableViewModelSwiftUI

struct StationsListView: View {
    @State var allStations: [StationLocation] = []
    @State private var searchText = ""
    @Environment(\.dismiss) private var dismiss
    var body: some View {
        NavigationStack {
            VStack {
                List(searchResults, id: \.crsCode) { station in
                    NavigationLink {
                        NearbyStationsContent(station: station)
                    } label: {
                        StationRowView(station: station)
                    }
                }
            }.navigationTitle("NR Stations")
        }
        .searchable(text: $searchText, prompt: "Enter station CRS code or name")
        .disableAutocorrection(true)
        .onDisappear {
            dismiss()
        }
    }
    var searchResults: [StationLocation] {
        if searchText.isEmpty {
            return allStations
        } else {
            return allStations.filter {
                (searchText.count == 3 && $0.crsCode == searchText.uppercased()) ||
                $0.stationName.starts(with: searchText)
            }
        }
    }
}

struct StationsLandingScreen: View {
    @StateViewModel var stationsViewModel = NrStationsViewModel()
    @State var allStations: [StationLocation] = []
    var body: some View {
        ZStack {
            VStack {
                switch stationsViewModel.uiState {
                case _ as StationsUiStateLoading:
                    Text("Loading...")
                case let uiState as StationsUiStateLoaded:
                    StationsListView(allStations: uiState.stations)
                case let uiState as StationsUiStateError:
                    if let error = uiState.error {
                        Text(error)
                            .foregroundColor(.red)
                    }
                default:
                    Text("Loading...")
                }
            }
        }.onAppear(perform: {
            NSLog("onAppear")
            stationsViewModel.getAllStations()
        })
        .refreshable {
            stationsViewModel.getAllStations()
        }
    }
}

struct StationRowView: View {
    let station: StationLocation
    var body: some View {
        HStack {
            Text(station.stationName)
            Spacer()
            Text(station.crsCode)
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
