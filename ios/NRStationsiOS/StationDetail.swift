//
//  StationDetail.swift
//  NRStationsiOS
//
//  Created by Awais Zaka on 01/11/2022.
//

import SwiftUI
import MapKit
import nrstations

struct StationDetail: View {
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
        }
        .navigationTitle(station.stationName)
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct StationDetail_Previews: PreviewProvider {
    static var previews: some View {
        StationDetail(station: StationLocation(stationName: "London Waterloo", crsCode: "WAT", latitude: 51.50329, longitude: -0.113108))
    }
}
