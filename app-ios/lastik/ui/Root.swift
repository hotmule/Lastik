//
//  Root.swift
//  lastik
//
//  Created by Timur Khatmullin on 29.05.2023.
//

import SwiftUI
import FeatureRoot
import Foundation

struct RootView: View {
    private let root: RootComponent
    
    @ObservedObject
    private var childStack: ObservableValue<ChildStack<AnyObject, RootComponentChild>>

    private var activeChild: RootComponentChild { childStack.value.active.instance }
    
    init(_ root: RootComponent) {
        self.root = root
        childStack = ObservableValue(root.stack)
    }
    
    var body: some View {
        switch activeChild {
        case let child as RootComponentChild.Auth: AuthView()
        case let child as RootComponentChild.Library: LibraryView()
        default: EmptyView()
        }
    }
}
