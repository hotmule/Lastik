//
//  RootHolder.swift
//  lastik
//
//  Created by Timur Khatmullin on 29.05.2023.
//

import FeatureRoot

class RootHolder : ObservableObject {
    let lifecycle: LifecycleRegistry
    let root: RootComponent

    init() {
        lifecycle = LifecycleRegistryKt.LifecycleRegistry()

        root = CommonInjector.init().provideRoot()(
            DefaultComponentContext(lifecycle: lifecycle)
        )

        LifecycleRegistryExtKt.create(lifecycle)
    }

    deinit {
        LifecycleRegistryExtKt.destroy(lifecycle)
    }
}
