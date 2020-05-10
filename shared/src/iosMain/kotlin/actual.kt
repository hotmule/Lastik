package com.jetbrains.handson.mpp.mobile

import platform.UIKit.UIDevice

actual fun platformName()
        = "${UIDevice.currentDevice.systemName()} ${UIDevice.currentDevice.systemVersion}"