//
//  KMMViewModel.swift
//  NRStationsiOS
//
//  Created by Awais Zaka on 12/03/2023.
//  Copyright © 2023 Intsoftdev. All rights reserved.
//

import Foundation
import nrstations
import KMPObservableViewModelCore
import KMPObservableViewModelSwiftUI
import Observation

// Needed for @StateViewModel / @ObservedViewModel / @EnvironmentViewModel
extension Kmp_observableviewmodel_coreViewModel: @retroactive ViewModel { }

// Optional but nice: Observation framework support (iOS 17+ etc.)
extension Kmp_observableviewmodel_coreViewModel: @retroactive Observable { }
