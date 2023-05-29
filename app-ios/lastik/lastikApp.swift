//
//  lastikApp.swift
//  lastik
//
//  Created by Timur Khatmullin on 27.05.2023.
//

import SwiftUI
import FeatureRoot

@main
struct lastikApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
        var appDelegate: AppDelegate

        @Environment(\.scenePhase)
        var scenePhase: ScenePhase

        var rootHolder: RootHolder { appDelegate.rootHolder }
    
    let root = CommonInjector.init().provideRoot()
    
    var body: some Scene {
        WindowGroup {
            RootView(rootHolder.root)
                .onChange(of: scenePhase) { newPhase in
                    switch newPhase {
                    case .background: LifecycleRegistryExtKt.stop(rootHolder.lifecycle)
                    case .inactive: LifecycleRegistryExtKt.pause(rootHolder.lifecycle)
                    case .active: LifecycleRegistryExtKt.resume(rootHolder.lifecycle)
                    @unknown default: break
                    }
                }
        }
    }
}
