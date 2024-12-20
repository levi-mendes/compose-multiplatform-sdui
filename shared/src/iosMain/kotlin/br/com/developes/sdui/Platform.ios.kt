package br.com.developes.sdui

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import br.com.developes.sdui.events.lifecycle.LifecycleComposeUIVCDelegate
import br.com.developes.sdui.events.lifecycle.LifecycleTracker
import br.com.developes.sdui.events.lifecycle.LocalLifecycleTracker
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIViewController

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    fun MainViewController(): UIViewController {
        val lifecycleTracker = LifecycleTracker()
        return ComposeUIViewController({
            delegate = LifecycleComposeUIVCDelegate(lifecycleTracker)
        }) {
            CompositionLocalProvider(LocalLifecycleTracker provides lifecycleTracker) {
                App(lifecycleTracker)
            }
        }
    }
}

actual fun getPlatform(): Platform = IOSPlatform()
actual fun generateUUID(): String = NSUUID().UUIDString()
actual fun openUrl(url: String?) {
    val nsUrl = url?.let { NSURL.URLWithString(it) } ?: return
    UIApplication.sharedApplication.openURL(nsUrl)
}
